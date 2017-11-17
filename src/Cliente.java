import java.util.ArrayList;
import java.util.List;

public class Cliente extends Usuario{

    private List<Pedido> pedidos = new ArrayList<>();
    private Pedido pedidoAtual = new Pedido();

    public Cliente(String usuario, String senha, String nome, String sobrenome, String email) {
        super(usuario, senha, nome, sobrenome, email);
    }

    public boolean autenticacao(String usuario, String senha) {
        return this.getUsuario().equals(usuario) && this.getSenha().equals(senha);
    }

    public List<Pedido> getPedidos() {
        return pedidos;
    }

    public Pedido getPedidoAtual() {
        return pedidoAtual;
    }

    public void setPedidoAtual(Pedido pedidoAtual) {
        this.pedidoAtual = pedidoAtual;
    }

    @Override
    public boolean equals(Object o){
        boolean igual = false;
        if( o instanceof Cliente && ((Cliente) o).getEmail().equals(this.getEmail())){
            igual = true;
        }
        return igual;
    }
}
