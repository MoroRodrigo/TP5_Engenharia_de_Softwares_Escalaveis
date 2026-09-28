import { useState } from "react";
import { criarProduto } from "../services/produtoService";

function ProdutoForm({ onProdutoSalvo }) {
    const [produto, setProduto] = useState({
        nome: "",
        descricao: "",
        preco: "",
        quantidade: ""
    });

    const handleChange = (e) => {
        setProduto({
            ...produto,
            [e.target.name]: e.target.value
        });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        try {
            await criarProduto({
                ...produto,
                preco: Number(produto.preco),
                quantidade: Number(produto.quantidade)
            });

            alert("Produto cadastrado com sucesso!");

            setProduto({
                nome: "",
                descricao: "",
                preco: "",
                quantidade: ""
            });

            onProdutoSalvo?.();
        } catch (error) {
            console.error(error);
            alert("Erro ao cadastrar produto.");
        }
    };

    return (
        <div className="form-container">
            <h2>Cadastrar Produto</h2>

            <form onSubmit={handleSubmit}>
                <input
                    type="text"
                    name="nome"
                    placeholder="Nome do Produto"
                    value={produto.nome}
                    onChange={handleChange}
                    required
                />

                <input
                    type="text"
                    name="descricao"
                    placeholder="Descrição"
                    value={produto.descricao}
                    onChange={handleChange}
                />

                <input
                    type="number"
                    name="preco"
                    placeholder="Preço"
                    min="0"
                    step="0.01"
                    value={produto.preco}
                    onChange={handleChange}
                    required
                />

                <input
                    type="number"
                    name="quantidade"
                    placeholder="Quantidade em Estoque"
                    min="0"
                    step="1"
                    value={produto.quantidade}
                    onChange={handleChange}
                    required
                />

                <button type="submit">Salvar Produto</button>
            </form>
        </div>
    );
}

export default ProdutoForm;
