import java.util.ArrayList;
import java.util.List;

public class Cliente {

    private List<Pedido> pedidos = new ArrayList<>();
    private Pedido pedidoAtual = new Pedido();
    private String usuario, senha;

    public Cliente(String usuario, String senha) {
        this.usuario = usuario;
        this.senha = senha;
    }

    public boolean autenticacao(String usuario, String senha) {
        return this.usuario.equals(usuario) && this.senha.equals(senha);
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
}
