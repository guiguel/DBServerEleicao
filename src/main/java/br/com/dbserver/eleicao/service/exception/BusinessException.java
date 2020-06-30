package br.com.dbserver.eleicao.service.exception;

public class BusinessException extends RuntimeException {

    public BusinessException(Message message) {
        super(message.name());
    }
}
