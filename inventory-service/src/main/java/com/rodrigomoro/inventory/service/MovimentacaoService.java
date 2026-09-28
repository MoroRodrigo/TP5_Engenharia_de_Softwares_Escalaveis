package com.rodrigomoro.inventory.service;

import com.rodrigomoro.inventory.dto.MovimentacaoResponse;
import com.rodrigomoro.inventory.dto.SaldoResponse;
import com.rodrigomoro.inventory.entity.MovimentacaoEstoque;
import com.rodrigomoro.inventory.entity.TipoMovimentacao;
import com.rodrigomoro.inventory.exception.EstoqueInsuficienteException;
import com.rodrigomoro.inventory.messaging.OutboxService;
import com.rodrigomoro.inventory.repository.MovimentacaoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class MovimentacaoService {
    private final MovimentacaoRepository repository;
    private final OutboxService outboxService;

    public MovimentacaoService(MovimentacaoRepository repository, OutboxService outboxService) {
        this.repository=repository; this.outboxService=outboxService;
    }

    @Transactional(readOnly=true)
    public SaldoResponse consultarSaldo(Long produtoId){
        Integer saldo=repository.calcularSaldo(produtoId,TipoMovimentacao.ENTRADA);
        return new SaldoResponse(produtoId,saldo==null?0:saldo);
    }

    @Transactional(readOnly=true)
    public List<MovimentacaoResponse> listarMovimentacoes(Long produtoId){
        return repository.findByProdutoIdOrderByDataHoraDescIdDesc(produtoId).stream().map(MovimentacaoResponse::from).toList();
    }

    @Transactional
    public void processarComando(String solicitacaoEventId,String correlationId,Long produtoId,
                                 String tipoTexto,Integer quantidade,String motivo){
        TipoMovimentacao tipo=lerTipo(tipoTexto);
        try{
            validarSaida(produtoId,tipo,quantidade);
            MovimentacaoResponse response=salvarMovimentacao(produtoId,tipo,quantidade,motivo);
            Integer saldoAtual=repository.calcularSaldo(produtoId,TipoMovimentacao.ENTRADA);
            outboxService.enqueue(
                    "MovimentacaoEstoqueRegistradaV1","Produto",produtoId.toString(),"estoque.movimentacao.registrada.v1",correlationId,
                    Map.of(
                            "solicitacaoEventId",solicitacaoEventId,
                            "produtoId",produtoId,
                            "movimentacaoId",response.id(),
                            "tipo",response.tipo(),
                            "quantidade",response.quantidade(),
                            "motivo",response.motivo()==null?"":response.motivo(),
                            "dataHora",response.dataHora(),
                            "saldoAtual",saldoAtual==null?0:saldoAtual
                    )
            );
        }catch(EstoqueInsuficienteException ex){
            outboxService.enqueue(
                    "MovimentacaoEstoqueRejeitadaV1","Produto",produtoId.toString(),"estoque.movimentacao.rejeitada.v1",correlationId,
                    Map.of(
                            "solicitacaoEventId",solicitacaoEventId,
                            "produtoId",produtoId,
                            "tipo",tipo.name(),
                            "quantidade",quantidade,
                            "motivo",ex.getMessage()
                    )
            );
        }
    }

    private MovimentacaoResponse salvarMovimentacao(Long produtoId,TipoMovimentacao tipo,Integer quantidade,String motivo){
        MovimentacaoEstoque entity=new MovimentacaoEstoque(produtoId,tipo,quantidade,motivo);
        return MovimentacaoResponse.from(repository.save(entity));
    }

    private void validarSaida(Long produtoId,TipoMovimentacao tipo,Integer quantidade){
        if(tipo==TipoMovimentacao.SAIDA){
            Integer saldoAtual=repository.calcularSaldo(produtoId,TipoMovimentacao.ENTRADA);
            int saldo=saldoAtual==null?0:saldoAtual;
            if(quantidade>saldo) throw new EstoqueInsuficienteException(produtoId,saldo,quantidade);
        }
    }

    private TipoMovimentacao lerTipo(String tipo){
        try{return TipoMovimentacao.valueOf(tipo.trim().toUpperCase());}
        catch(Exception ex){throw new IllegalArgumentException("Tipo deve ser ENTRADA ou SAIDA");}
    }
}
