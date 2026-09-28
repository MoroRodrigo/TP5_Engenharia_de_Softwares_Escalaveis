import { useEffect, useState } from "react";

import { listarProdutos, excluirProduto } from "../services/produtoService";

function ProdutoList({
    atualizacao,
    onProdutosCarregados,
    onSelecionarProduto,
    produtoSelecionadoId
}) {
    const [produtos, setProdutos] = useState([]);

    const carregarProdutos = async () => {
        try {
            const response = await listarProdutos();
            const dados = response.data;
            const lista = Array.isArray(dados) ? dados : dados.content || [];
            setProdutos(lista);
            onProdutosCarregados?.(lista);
        } catch (error) {
            console.error(error);
        }
    };

    useEffect(() => {
        carregarProdutos();
    }, [atualizacao]);

    const remover = async (id) => {
        if (!window.confirm("Deseja realmente excluir este produto?")) {
            return;
        }

        try {
            await excluirProduto(id);
            if (Number(produtoSelecionadoId) === id) {
                onSelecionarProduto?.(null);
            }
            carregarProdutos();
        } catch (error) {
            console.error(error);
        }
    };

    return (
        <div className="lista-container">
            <div className="section-heading">
                <div>
                    <span className="eyebrow">MONÓLITO</span>
                    <h2>Produtos cadastrados</h2>
                </div>
                <span className="section-hint">Selecione um produto para acessar o microsserviço</span>
            </div>

            <table>
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Nome</th>
                    <th>Descrição</th>
                    <th>Preço</th>
                    <th>Qtd. cadastrada</th>
                    <th>Ações</th>
                </tr>
                </thead>

                <tbody>
                {produtos.length > 0 ? (
                    produtos.map((produto) => (
                        <tr key={produto.id} className={Number(produtoSelecionadoId) === produto.id ? "linha-selecionada" : ""}>
                            <td>{produto.id}</td>
                            <td>{produto.nome}</td>
                            <td>{produto.descricao}</td>
                            <td>R$ {Number(produto.preco).toFixed(2)}</td>
                            <td>{produto.quantidade}</td>
                            <td className="acoes">
                                <button
                                    className="btn-estoque"
                                    onClick={() => onSelecionarProduto?.(produto.id)}
                                >
                                    Ver estoque
                                </button>
                                <button
                                    className="btn-excluir"
                                    onClick={() => remover(produto.id)}
                                >
                                    Excluir
                                </button>
                            </td>
                        </tr>
                    ))
                ) : (
                    <tr>
                        <td colSpan="6">Nenhum produto cadastrado.</td>
                    </tr>
                )}
                </tbody>
            </table>
        </div>
    );
}

export default ProdutoList;
