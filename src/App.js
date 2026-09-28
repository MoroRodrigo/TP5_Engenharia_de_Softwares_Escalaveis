import "./App.css";
import { useState } from "react";
import ProdutoForm from "./components/ProdutoForm";
import ProdutoList from "./components/ProdutoList";
import EstoquePanel from "./components/EstoquePanel";

function App() {
    const [atualizacao, setAtualizacao] = useState(0);
    const [produtoSelecionadoId, setProdutoSelecionadoId] = useState(null);
    const [produtos, setProdutos] = useState([]);

    const produtoSalvo = () => setAtualizacao((valor) => valor + 1);

    return (
        <div className="app">
            <header className="header">
                <span className="eyebrow">TP5 - ENGENHARIA DE SOFTWARES ESCALÁVEIS</span>
                <h1>Sistema de Gestão de Produtos e Estoque</h1>
                <p>Monólito Spring Boot + microsserviço de inventário com Spring Cloud e React</p>
            </header>

            <ProdutoForm onProdutoSalvo={produtoSalvo} />
            <ProdutoList
                atualizacao={atualizacao}
                onProdutosCarregados={setProdutos}
                onSelecionarProduto={setProdutoSelecionadoId}
                produtoSelecionadoId={produtoSelecionadoId}
            />
            <EstoquePanel
                produtos={produtos}
                produtoSelecionadoId={produtoSelecionadoId}
            />
        </div>
    );
}

export default App;
