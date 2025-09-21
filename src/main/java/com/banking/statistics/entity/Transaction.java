package com.banking.statistics.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "Operation date is required")
    @Column(name = "fecha_operacion", nullable = false)
    private LocalDate fechaOperacion;
    
    @NotNull(message = "Value date is required")
    @Column(name = "fecha_valor", nullable = false)
    private LocalDate fechaValor;
    
    @Size(max = 255, message = "Concept must not exceed 255 characters")
    @Column(name = "concepto", length = 255)
    private String concepto;
    
    @DecimalMin(value = "0.0", inclusive = true, message = "Payments must be non-negative")
    @Column(name = "pagos", precision = 15, scale = 2)
    private BigDecimal pagos = BigDecimal.ZERO;
    
    @DecimalMin(value = "0.0", inclusive = true, message = "Income must be non-negative")
    @Column(name = "ingresos", precision = 15, scale = 2)
    private BigDecimal ingresos = BigDecimal.ZERO;
    
    @NotNull(message = "Balance is required")
    @Column(name = "saldo", nullable = false, precision = 15, scale = 2)
    private BigDecimal saldo;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Category category;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_email", nullable = false)
    @JsonBackReference
    private User user;
    
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

}