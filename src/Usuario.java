public class Usuario {

    private String usuario, senha, nome, sobrenome, email;

    public Usuario(String usuario, String senha, String nome, String sobrenome, String email) {
        this.usuario = usuario;
        this.senha = senha;
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.email = email;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getSenha() {
        return senha;
    }

    public String getNome() {
        return nome;
    }

    public String getSobrenome(){return sobrenome;}

    public String getEmail(){return email;}

    public boolean autenticacao(String usuario, String senha) {
        return this.usuario.equals(usuario) && this.senha.equals(senha);
    }

}
