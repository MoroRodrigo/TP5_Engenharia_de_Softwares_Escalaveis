import axios from 'axios';

// Em produção no Docker, o React é servido pelo Nginx em localhost:3001.
// As chamadas /api usam a mesma origem e o Nginx encaminha para o backend.
const api = axios.create({
  baseURL: '/api/produtos',
  headers: {
    'Content-Type': 'application/json'
  }
});

export const listarProdutos = async () => {
  return api.get('');
};

export const criarProduto = async (produto) => {
  return api.post('', produto);
};

export const atualizarProduto = async (id, produto) => {
  return api.put(`/${id}`, produto);
};

export const excluirProduto = async (id) => {
  return api.delete(`/${id}`);
};
