import javax.swing.JFrame;

public class App {
    public static void main(String[] args) throws Exception {
        // Configurações do tabuleiro
        int linhas = 21;          // Número de linhas no mapa
        int colunas = 19;         // Número de colunas no mapa
        int tamanhoBloco = 32;    // Tamanho de cada bloco (em pixels)
        int larguraTabuleiro = colunas * tamanhoBloco;  // Largura total da janela
        int alturaTabuleiro = linhas * tamanhoBloco;   // Altura total da janela

        // Configuração da janela principal
        JFrame janela = new JFrame("Pac-Man");
        janela.setSize(larguraTabuleiro, alturaTabuleiro);
        janela.setLocationRelativeTo(null);  // Centraliza a janela na tela
        janela.setResizable(false);          // Impede redimensionamento
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // Fecha o programa ao sair

        // Inicializa o jogo e adiciona à janela
        JogoPacMan jogoPacMan = new JogoPacMan();
        janela.add(jogoPacMan);
        janela.pack();                      // Ajusta o tamanho da janela automaticamente
        jogoPacMan.requestFocus();          // Permite que o painel receba entrada do teclado
        janela.setVisible(true);            // Torna a janela visível
    }
}