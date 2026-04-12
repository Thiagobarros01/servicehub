package thiagosbarros.com.servicehub.entity;

import jakarta.persistence.*;
import thiagosbarros.com.servicehub.entity.enums.MetodoPagamento;
import thiagosbarros.com.servicehub.entity.enums.StatusPagamento;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "pagamento")
public class Pagamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agendamento_id", nullable = false)
    private Agendamento agendamento;

    @Column(name = "valor", nullable = false)
    private BigDecimal valor;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusPagamento status;

    @Column(name = "metodo", nullable = false)
    @Enumerated(EnumType.STRING)
    private MetodoPagamento metodoPagamento;

    @Column(name = "data_pagamento")
    private LocalDateTime dataPagamento;

    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private LocalDateTime createdAt;

    protected Pagamento() {
    }

    public Pagamento(Agendamento agendamento, BigDecimal valor, StatusPagamento status, MetodoPagamento metodoPagamento, LocalDateTime dataPagamento) {
        this.agendamento = agendamento;
        this.valor = valor;
        this.status = status;
        this.metodoPagamento = metodoPagamento;
        this.dataPagamento = dataPagamento;
    }

    public Long getId() {
        return id;
    }

    public Agendamento getAgendamento() {
        return agendamento;
    }

    public void setAgendamento(Agendamento agendamento) {
        this.agendamento = agendamento;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public StatusPagamento getStatus() {
        return status;
    }

    public void setStatus(StatusPagamento status) {
        this.status = status;
    }

    public MetodoPagamento getMetodoPagamento() {
        return metodoPagamento;
    }

    public void setMetodoPagamento(MetodoPagamento metodoPagamento) {
        this.metodoPagamento = metodoPagamento;
    }

    public LocalDateTime getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(LocalDateTime dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pagamento pagamento)) return false;
        return id != null && Objects.equals(id, pagamento.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
