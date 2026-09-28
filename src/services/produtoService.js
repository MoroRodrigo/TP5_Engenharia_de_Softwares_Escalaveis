import axios from 'axios';

const API_BASE_URL = process.env.REACT_APP_API_BASE_URL || '';

const api = axios.create({
  baseURL: `${API_BASE_URL}/api/produtos`,
  headers: { 'Content-Type': 'application/json' }
});

/**
 * Retorna a resposta Axios para manter o mesmo contrato usado
 * pelos componentes do front-end.
 */
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
