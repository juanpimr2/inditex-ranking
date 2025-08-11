package io.github.juanpimr2.inditex_ranking.service.base;

public abstract class BaseService<REQ, RES> {

    public final RES process(REQ request) {
        validate(request);
        return doProcess(request);
    }

    /** Valida la request. Lanza excepción controlada si no cumple. */
    protected abstract void validate(REQ request);

    /** Lógica principal del caso de uso/servicio. */
    protected abstract RES doProcess(REQ request);
}
