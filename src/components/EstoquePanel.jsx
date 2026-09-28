import { useEffect, useState } from "react";
import {
    consultarSaldoEstoque,
    listarMovimentacoes,
    registrarMovimentacao
} from "../services/estoqueService";

function aguardar(milissegundos) {
    return new Promise((resolve) => setTimeout(resolve, milissegundos));
}

function EstoquePanel({ produtos, produtoSelecionadoId }) {
    const [saldo, setSaldo] = useState(null);
    const [movimentacoes, setMovimentacoes] = useState([]);
    const [form, setForm] = useState({ tipo: "ENTRADA", quantidade: "", motivo: "" });
    const [erro, setErro] = useState("");
    const [mensagem, setMensagem] = useState("");
    const produto = produtos.find((item) => item.id === Number(produtoSelecionadoId));

    const carregar = async () => {
        if (!produtoSelecionadoId) {
            setSaldo(null);
            setMovimentacoes([]);
            return [];
        }

        try {
            setErro("");
            const [saldoResponse, movimentosResponse] = await Promise.all([
                consultarSaldoEstoque(produtoSelecionadoId),
                listarMovimentacoes(produtoSelecionadoId)
            ]);
            setSaldo(saldoResponse.data.saldoAtual);
            setMovimentacoes(movimentosResponse.data);
            return movimentosResponse.data;
        } catch (error) {
            console.error(error);
            setErro("Não foi possível consultar o estoque. Verifique se o monólito, RabbitMQ e inventory-service estão ativos.");
            return [];
        }
    };

    useEffect(() => {
        carregar();
    }, [produtoSelecionadoId]);

    const alterarForm = (event) => {
        setForm({ ...form, [event.target.name]: event.target.value });
    };

    const enviar = async (event) => {
        event.preventDefault();
        try {
            setErro("");
            setMensagem("");
            const response = await registrarMovimentacao(produtoSelecionadoId, {
                ...form,
                quantidade: Number(form.quantidade)
            });
            setForm({ tipo: "ENTRADA", quantidade: "", motivo: "" });
            setMensagem(`Evento ${response.data.eventId} enviado. Aguardando processamento...`);

            for (let tentativa = 0; tentativa < 6; tentativa += 1) {
                await aguardar(500);
                const itens = await carregar();
                const processamento = itens.find((item) => item.eventId === response.data.eventId);
                if (processamento && processamento.status !== "PROCESSANDO") {
                    if (processamento.status === "REGISTRADA") {
                        setMensagem("Movimentação processada com sucesso pelo microsserviço.");
                    } else {
                        setErro(processamento.erro || "A movimentação foi rejeitada pelo microsserviço.");
                        setMensagem("");
                    }
                    break;
                }
            }
        } catch (error) {
            console.error(error);
            setErro(error.response?.data?.message || "Não foi possível enviar a movimentação.");
            setMensagem("");
        }
    };

    return (
        <section className="estoque-panel">
            <div className="estoque-panel-header">
                <div>
                    <span className="eyebrow">ARQUITETURA ORIENTADA A EVENTOS</span>
                    <h2>Estoque e movimentações</h2>
                </div>
                {produto && (
                    <div className="produto-contexto">
                        <strong>{produto.nome}</strong>
                        <span>Produto #{produto.id}</span>
                    </div>
                )}
            </div>

            {!produtoSelecionadoId ? (
                <p className="empty-message">Selecione um produto para consultar o estoque.</p>
            ) : (
                <>
                    {erro && <div className="error-message">{erro}</div>}
                    {mensagem && <div className="success-message">{mensagem}</div>}

                    <div className="saldo-card">
                        <span>Saldo atual projetado</span>
                        <strong>{saldo ?? "-"}</strong>
                        <small>Atualizado a partir de eventos publicados pelo inventory-service.</small>
                    </div>

                    <form className="movimentacao-form" onSubmit={enviar}>
                        <select name="tipo" value={form.tipo} onChange={alterarForm}>
                            <option value="ENTRADA">Entrada</option>
                            <option value="SAIDA">Saída</option>
                        </select>
                        <input
                            type="number"
                            name="quantidade"
                            min="1"
                            placeholder="Quantidade"
                            value={form.quantidade}
                            onChange={alterarForm}
                            required
                        />
                        <input
                            type="text"
                            name="motivo"
                            placeholder="Motivo"
                            value={form.motivo}
                            onChange={alterarForm}
                        />
                        <button type="submit">Enviar evento</button>
                    </form>

                    <div className="movimentacoes-box">
                        <h3>Fila lógica de movimentações</h3>
                        {movimentacoes.length === 0 ? (
                            <p className="empty-message">Nenhuma solicitação encontrada.</p>
                        ) : (
                            <div className="movimentacoes-lista">
                                {movimentacoes.map((mov) => (
                                    <div className="movimentacao-item" key={mov.eventId}>
                                        <span className={`badge badge-${mov.tipo.toLowerCase()}`}>{mov.tipo}</span>
                                        <strong>{mov.quantidade} un.</strong>
                                        <span>{mov.motivo || "Sem motivo"}</span>
                                        <span className={`status status-${mov.status.toLowerCase()}`}>{mov.status}</span>
                                        <small>{new Date(mov.criadaEm).toLocaleString("pt-BR")}</small>
                                    </div>
                                ))}
                            </div>
                        )}
                    </div>
                </>
            )}
        </section>
    );
}

export default EstoquePanel;
