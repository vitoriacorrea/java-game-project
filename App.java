import javax.swing.JFrame;

public class App {
    public static void main(String[] args) throws Exception {
        // Configurações do tabuleiro
        int quantidadeLinhas = 21;       // Número de linhas no tabuleiro
        int quantidadeColunas = 19;      // Número de colunas no tabuleiro
        int tamanhoBloco = 32;           // Tamanho de cada bloco/tile em pixels
        int larguraTabuleiro = quantidadeColunas * tamanhoBloco;  // Largura total
        int alturaTabuleiro = quantidadeLinhas * tamanhoBloco;    // Altura total

        // Configuração da janela principal
        JFrame janela = new JFrame("Pac Man");
        janela.setSize(larguraTabuleiro, alturaTabuleiro);
        janela.setLocationRelativeTo(null);  // Centraliza a janela
        janela.setResizable(false);          // Impede redimensionamento
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // Fecha o programa ao sair

        // Cria e adiciona o jogo PacMan à janela
        PacMan jogoPacMan = new PacMan();
        janela.add(jogoPacMan);
        janela.pack();                      // Ajusta o tamanho da janela
        jogoPacMan.requestFocus();          // Foco para receber inputs do teclado
        janela.setVisible(true);            // Torna a janela visível
    }
}