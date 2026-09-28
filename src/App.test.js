import { render, screen } from "@testing-library/react";
import App from "./App";

jest.mock("./services/produtoService", () => ({
    listarProdutos: jest.fn(() => Promise.resolve({ data: { content: [] } })),
    criarProduto: jest.fn(),
    excluirProduto: jest.fn()
}));

jest.mock("./services/estoqueService", () => ({
    consultarSaldoEstoque: jest.fn(() => Promise.resolve({ data: { saldoAtual: 0 } })),
    listarMovimentacoes: jest.fn(() => Promise.resolve({ data: [] })),
    registrarMovimentacao: jest.fn()
}));

test("renderiza a aplicação integrada com microsserviço", async () => {
    render(<App />);
    expect(screen.getByText(/Sistema de Gestão de Produtos e Estoque/i)).toBeInTheDocument();
    expect(screen.getByText(/Estoque e movimentações/i)).toBeInTheDocument();
});
