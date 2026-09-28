import axios from 'axios';

const API_BASE_URL = process.env.REACT_APP_API_BASE_URL || '';

const api = axios.create({
  baseURL: `${API_BASE_URL}/api/produtos`,
  headers: { 'Content-Type': 'application/json' }
});

/**
 * Retorna a resposta Axios para que os componentes possam tratar
 * o corpo HTTP por meio de response.data.
 */
export const consultarSaldoEstoque = async (produtoId) => {
  return api.get(`/${produtoId}/estoque`);
};

/**
 * Lista as movimentações do produto.
 */
export const listarMovimentacoes = async (produtoId) => {
  return api.get(`/${produtoId}/movimentacoes`);
};

/**
 * Solicita uma movimentação de estoque de forma assíncrona.
 */
export const registrarMovimentacao = async (produtoId, movimentacao) => {
  return api.post(`/${produtoId}/movimentacoes`, movimentacao);
};
