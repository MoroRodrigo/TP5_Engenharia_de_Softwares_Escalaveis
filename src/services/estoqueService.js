import axios from 'axios';

const api = axios.create({
  baseURL: '/api/produtos',
  headers: {
    'Content-Type': 'application/json'
  }
});

export const consultarSaldoEstoque = async (produtoId) => {
  return api.get(`/${produtoId}/estoque`);
};

export const listarMovimentacoes = async (produtoId) => {
  return api.get(`/${produtoId}/movimentacoes`);
};

export const registrarMovimentacao = async (produtoId, movimentacao) => {
  return api.post(`/${produtoId}/movimentacoes`, movimentacao);
};
