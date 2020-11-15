package br.coop.unimed.giuclientsso.enumerator;

public enum Erro {

    DEFAULT(0, "Erro durante o uso."),
    AUTH_TOKEN_NAO_INFORMADO(1, "Não foi informado o Authentication Code."),
    USUARIO_NAO_POSSUI_UNIMED(2, "O usuário não possui a Unimed informada."),
    USUARIO_NAO_POSSUI_APLICACAO(3, "O usuário não possui acesso a aplicação."),
    NAO_INFORMADO_UNIMED(4, "Não foi informada a unimed do usuário."),
    AUTH_TOKEN_COOKIE_NAO_INFORMADO(5, "Não foi informado o Authentication Code e/ou Cookie."),
    SESSAO_EXPIRADA(6, "Sua sessão está foi expirada. Favor realizar o login novamente.");





    private String codigo;
    private String mensagem;

    Erro(Integer codigo, String mensagem) {
        this.codigo = "SSO-CLIENT-" + String.format("%04d", codigo);
        this.mensagem = mensagem;
    }

    public String getCodigo() {
        return this.codigo;
    }

    public String getMensagem() {
        return this.mensagem;
    }
}
