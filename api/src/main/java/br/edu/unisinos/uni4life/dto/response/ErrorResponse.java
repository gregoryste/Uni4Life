package br.edu.unisinos.uni4life.dto.response;


import static br.edu.unisinos.uni4life.domain.enumeration.ErrorType.FORBIDDEN;
import static br.edu.unisinos.uni4life.domain.enumeration.ErrorType.INTERNAL_ERROR;
import static br.edu.unisinos.uni4life.domain.enumeration.ErrorType.NOT_AUTHORIZED;
import static br.edu.unisinos.uni4life.domain.enumeration.ErrorType.NOT_FOUND;
import static br.edu.unisinos.uni4life.domain.enumeration.ErrorType.VALIDATION;
import static java.lang.String.format;
import static java.util.Collections.emptyMap;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.BooleanUtils.negate;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import br.edu.unisinos.uni4life.domain.enumeration.ErrorType;
import br.edu.unisinos.uni4life.exception.AbstractErrorException;
import br.edu.unisinos.uni4life.exception.ClientErrorException;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Data
@Builder
public final class ErrorResponse implements Serializable {

    private static final long serialVersionUID = 5510253969993856060L;

    private ErrorType errorType;
    private String message;
    private String field;
    private Map<String, String> details;

    public static ErrorResponse build(final Exception exception) {

        if (exception instanceof AbstractErrorException) {
            return build(exception);
        }

        if (exception instanceof MethodArgumentNotValidException) {
            return build(exception);
        }

        if (exception instanceof HttpMessageNotReadableException) {
            return build(exception);
        }

        return buildDefault();
    }

    public static ErrorResponse build(final AbstractErrorException ex) {
        return ErrorResponse.builder()
            .errorType(ex.getErrorType())
            .message(ex.getMessage())
            .field(ex.getField())
            .details(negate(ex.getDetails().isEmpty()) ? ex.getDetails() : emptyMap())
            .build();
    }

    public static ErrorResponse build(final MethodArgumentNotValidException ex) {
        final List<CampoErro> erros = getErrors(ex);
        final CampoErro campo = erros.stream()
            .findFirst()
            .orElse(new CampoErro(null, "Requisição inválida"));

        return ErrorResponse.builder()
            .errorType(VALIDATION)
            .message(campo.getMessage())
            .field(campo.getNome())
            .details(emptyMap())
            .build();
    }

    public static ErrorResponse build(final MissingServletRequestParameterException ex) {
        final String parametro = ex.getParameterName();

        return ErrorResponse.builder()
            .errorType(VALIDATION)
            .message(format("Parâmetro '%s' inválido ou não informado.", parametro))
            .field(parametro)
            .details(emptyMap())
            .build();
    }

    public static ErrorResponse build(final MethodArgumentTypeMismatchException ex) {
        final String parametro = ex.getName();

        return ErrorResponse.builder()
            .errorType(VALIDATION)
            .message(format("Parâmetro '%s' inválido ou não informado.", parametro))
            .field(parametro)
            .details(emptyMap())
            .build();
    }

    public static ErrorResponse build(final UsernameNotFoundException ex) {
        return ErrorResponse.builder()
            .errorType(NOT_FOUND)
            .message("Usuário não encontrado.")
            .details(emptyMap())
            .build();
    }

    public static ErrorResponse build(final InsufficientAuthenticationException ex) {
        return ErrorResponse.builder()
            .errorType(NOT_AUTHORIZED)
            .message("Usuário não autenticado.")
            .details(emptyMap())
            .build();
    }

    public static ErrorResponse build(final AccessDeniedException ex) {
        return ErrorResponse.builder()
            .errorType(FORBIDDEN)
            .message("Usuário não autorizado.")
            .details(emptyMap())
            .build();
    }

    public static ErrorResponse build(final HttpMessageNotReadableException ex) {
        return ErrorResponse.builder()
            .errorType(VALIDATION)
            .message(getMessageFromCause(ex.getCause()))
            .details(getDetailsFromCause(ex.getCause()))
            .build();
    }

    public static ErrorResponse buildDefault() {
        return ErrorResponse.builder()
            .errorType(INTERNAL_ERROR)
            .message("Não foi possível realizar operação, tente novamente mais tarde.")
            .details(emptyMap())
            .build();
    }

    private static String getMessageFromCause(final Throwable throwable) {
        return ofNullable(throwable)
            .map(Throwable::getCause)
            .filter(ClientErrorException.class::isInstance)
            .map(Throwable::getMessage)
            .orElse("Requisição inválida.");
    }

    private static Map<String, String> getDetailsFromCause(final Throwable throwable) {
        return ofNullable(throwable)
            .map(Throwable::getCause)
            .filter(ClientErrorException.class::isInstance)
            .map(ClientErrorException.class::cast)
            .map(ClientErrorException::getDetails)
            .orElse(emptyMap());
    }

    private static List<CampoErro> getErrors(final MethodArgumentNotValidException exception) {
        return ofNullable(exception)
            .map(BindException::getBindingResult)
            .map(Errors::getFieldErrors)
            .orElseGet(Collections::emptyList)
            .stream()
            .map(fieldError -> new CampoErro(fieldError.getField(), fieldError.getDefaultMessage()))
            .collect(Collectors.toList());
    }

    @Getter
    @RequiredArgsConstructor
    private static class CampoErro implements Serializable {

        private static final long serialVersionUID = -1072347796227573688L;

        private final String nome;
        private final String message;
    }
}
