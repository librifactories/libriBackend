package processos;

public class Funcionario extends Usuario {

    private String cargo;

    public Funcionario(String usuario, String senha, String nome, String cargo, String sobrenome, String email) {
        super(usuario, senha, nome, sobrenome, email);
        this.cargo = cargo;
    }

    public String getCargo() {
        return cargo;
    }
}
