package com.meli.service.backend.mutant.entities;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonPropertyOrder({"status", "body"})
public class MeLiResponseEntity<T> extends BaseResponseEntity {
    protected transient T body;
}