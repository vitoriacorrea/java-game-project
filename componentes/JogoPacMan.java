import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Random;
import javax.swing.*;

public class JogoPacMan extends JPanel implements ActionListener, KeyListener {
    // Classe interna que representa um bloco (paredes, fantasmas, Pac-Man, comidas)
    class Bloco {
        int x, y;               // Posição atual
        int largura, altura;    // Dimensões
        Image imagem;           // Imagem do bloco

        int posInicialX;        // Posição inicial (para resetar)
        int posInicialY;
        char direcao = 'C';     // Direção (C = Cima, B = Baixo, E = Esquerda, D = Direita)
        int velocidadeX = 0;    // Velocidade horizontal
        int velocidadeY = 0;    // Velocidade vertical

        // Construtor do bloco
        Bloco(Image imagem, int x, int y, int largura, int altura) {
            this.imagem = imagem;
            this.x = x;
            this.y = y;
            this.largura = largura;
            this.altura = altura;
            this.posInicialX = x;
            this.posInicialY = y;
        }

        // Atualiza a direção e verifica colisões com paredes
        void atualizarDirecao(char novaDirecao) {
            char direcaoAnterior = this.direcao;
            this.direcao = novaDirecao;
            atualizarVelocidade();
            
            // Testa movimento antes de confirmar
            this.x += this.velocidadeX;
            this.y += this.velocidadeY;
            
            // Verifica colisão com paredes
            for (Bloco parede : paredes) {
                if (colisao(this, parede)) {
                    this.x -= this.velocidadeX;
                    this.y -= this.velocidadeY;
                    this.direcao = direcaoAnterior;  // Volta para a direção anterior
                    atualizarVelocidade();
                }
            }
        }

        // Define a velocidade com base na direção
        void atualizarVelocidade() {
            if (this.direcao == 'C') {       // Cima
                this.velocidadeX = 0;
                this.velocidadeY = -tamanhoBloco / 4;
            } else if (this.direcao == 'B') { // Baixo
                this.velocidadeX = 0;
                this.velocidadeY = tamanhoBloco / 4;
            } else if (this.direcao == 'E') { // Esquerda
                this.velocidadeX = -tamanhoBloco / 4;
                this.velocidadeY = 0;
            } else if (this.direcao == 'D') { // Direita
                this.velocidadeX = tamanhoBloco / 4;
                this.velocidadeY = 0;
            }
        }

        // Reseta o bloco para a posição inicial
        void resetar() {
            this.x = this.posInicialX;
            this.y = this.posInicialY;
        }
    }

    // Configurações do jogo
    private int linhas = 21;
    private int colunas = 19;
    private int tamanhoBloco = 32;
    private int larguraTabuleiro = colunas * tamanhoBloco;
    private int alturaTabuleiro = linhas * tamanhoBloco;

    // Imagens do jogo
    private Image imgParede;
    private Image imgFantasmaAzul;
    private Image imgFantasmaLaranja;
    private Image imgFantasmaAmarelo;
    private Image imgFantasmaVermelho;

    private Image imgPacManCima;
    private Image imgPacManBaixo;
    private Image imgPacManEsquerda;
    private Image imgPacManDireita;

    // Mapa do jogo (X = Parede, O = Vazio, P = Pac-Man, ' ' = Comida)
    // Fantasmas: b = Azul, o = Laranja, p = amarelo, r = Vermelho
    private String[] mapa = {
        "XXXXXXXXXXXXXXXXXXX",
        "X        X        X",
        "X XX XXX X XXX XX X",
        "X                 X",
        "X XX X XXXXX X XX X",
        "X    X       X    X",
        "XXXX XXXX XXXX XXXX",
        "OOOX X       X XOOO",
        "XXXX X XXrXX X XXXX",
        "O       bpo       O",
        "XXXX X XXXXX X XXXX",
        "OOOX X       X XOOO",
        "XXXX X XXXXX X XXXX",
        "X        X        X",
        "X XX XXX X XXX XX X",
        "X  X     P     X  X",
        "XX X X XXXXX X X XX",
        "X    X   X   X    X",
        "X XXXXXX X XXXXXX X",
        "X                 X",
        "XXXXXXXXXXXXXXXXXXX"
    };

    // Conjuntos de elementos do jogo
    HashSet<Bloco> paredes;     // Todas as paredes
    HashSet<Bloco> comidas;     // Todas as comidas
    HashSet<Bloco> fantasmas;   // Todos os fantasmas
    Bloco pacMan;               // O jogador (Pac-Man)

    // Controle do jogo
    Timer loopJogo;             // Timer para atualizar o jogo
    char[] direcoes = {'C', 'B', 'E', 'D'};  // Possíveis direções
    Random random = new Random();  // Gerador de números aleatórios
    int pontuacao = 0;          // Pontuação atual
    int vidas = 3;              // Número de vidas
    boolean jogoTerminou = false;  // Indica se o jogo acabou

    // Construtor
    JogoPacMan() {
        setPreferredSize(new Dimension(larguraTabuleiro, alturaTabuleiro));
        setBackground(Color.BLACK);
        addKeyListener(this);
        setFocusable(true);

        // Carrega as imagens
        imgParede = new ImageIcon(getClass().getResource("./Parede.png")).getImage();
        imgFantasmaAzul = new ImageIcon(getClass().getResource("./FantasmaAzul.png")).getImage();
        imgFantasmaLaranja = new ImageIcon(getClass().getResource("./FantasmaLaranja.png")).getImage();
        imgFantasmaAmarelo = new ImageIcon(getClass().getResource("./FantasmaRosa.png")).getImage();
        imgFantasmaVermelho = new ImageIcon(getClass().getResource("./FantasmaVermelho.png")).getImage();

        imgPacManCima = new ImageIcon(getClass().getResource("./pacmanCima.png")).getImage();
        imgPacManBaixo = new ImageIcon(getClass().getResource("./pacmanBaixo.png")).getImage();
        imgPacManEsquerda = new ImageIcon(getClass().getResource("./pacmanEsquerda.png")).getImage();
        imgPacManDireita = new ImageIcon(getClass().getResource("./pacmanDireita.png")).getImage();

        carregarMapa();

        // Define direções aleatórias para os fantasmas
        for (Bloco fantasma : fantasmas) {
            char novaDirecao = direcoes[random.nextInt(4)];
            fantasma.atualizarDirecao(novaDirecao);
        }

        // Configura o timer do jogo (20 FPS)
        loopJogo = new Timer(50, this);
        loopJogo.start();
    }

    // Carrega o mapa e posiciona os elementos
    public void carregarMapa() {
        paredes = new HashSet<Bloco>();
        comidas = new HashSet<Bloco>();
        fantasmas = new HashSet<Bloco>();

        for (int linha = 0; linha < linhas; linha++) {
            for (int coluna = 0; coluna < colunas; coluna++) {
                String linhaMapa = mapa[linha];
                char caractere = linhaMapa.charAt(coluna);

                int x = coluna * tamanhoBloco;
                int y = linha * tamanhoBloco;

                if (caractere == 'X') {  // Parede
                    Bloco parede = new Bloco(imgParede, x, y, tamanhoBloco, tamanhoBloco);
                    paredes.add(parede);
                } else if (caractere == 'b') {  // Fantasma Azul
                    Bloco fantasma = new Bloco(imgFantasmaAzul, x, y, tamanhoBloco, tamanhoBloco);
                    fantasmas.add(fantasma);
                } else if (caractere == 'o') {  // Fantasma Laranja
                    Bloco fantasma = new Bloco(imgFantasmaLaranja, x, y, tamanhoBloco, tamanhoBloco);
                    fantasmas.add(fantasma);
                } else if (caractere == 'p') {  // Fantasma Rosa
                    Bloco fantasma = new Bloco(imgFantasmaAmarelo, x, y, tamanhoBloco, tamanhoBloco);
                    fantasmas.add(fantasma);
                } else if (caractere == 'r') {  // Fantasma Vermelho
                    Bloco fantasma = new Bloco(imgFantasmaVermelho, x, y, tamanhoBloco, tamanhoBloco);
                    fantasmas.add(fantasma);
                } else if (caractere == 'P') {  // Pac-Man
                    pacMan = new Bloco(imgPacManDireita, x, y, tamanhoBloco, tamanhoBloco);
                } else if (caractere == ' ') {  // Comida (pequeno ponto branco)
                    Bloco comida = new Bloco(null, x + 14, y + 14, 4, 4);
                    comidas.add(comida);
                }
            }
        }
    }

    // Desenha o jogo na tela
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        desenhar(g);
    }

    public void desenhar(Graphics g) {
        // Desenha o Pac-Man
        g.drawImage(pacMan.imagem, pacMan.x, pacMan.y, pacMan.largura, pacMan.altura, null);

        // Desenha os fantasmas
        for (Bloco fantasma : fantasmas) {
            g.drawImage(fantasma.imagem, fantasma.x, fantasma.y, fantasma.largura, fantasma.altura, null);
        }

        // Desenha as paredes
        for (Bloco parede : paredes) {
            g.drawImage(parede.imagem, parede.x, parede.y, parede.largura, parede.altura, null);
        }

        // Desenha as comidas (pontos brancos)
        g.setColor(Color.WHITE);
        for (Bloco comida : comidas) {
            g.fillRect(comida.x, comida.y, comida.largura, comida.altura);
        }

        // Mostra a pontuação e vidas
        g.setFont(new Font("Arial", Font.PLAIN, 18));
        if (jogoTerminou) {
            g.drawString("Fim de Jogo: " + pontuacao, tamanhoBloco / 2, tamanhoBloco / 2);
        } else {
            g.drawString("x" + vidas + " Pontos: " + pontuacao, tamanhoBloco / 2, tamanhoBloco / 2);
        }
    }

    // Atualiza a lógica do jogo
    public void mover() {
        // Movimenta o Pac-Man
        pacMan.x += pacMan.velocidadeX;
        pacMan.y += pacMan.velocidadeY;

        // Verifica colisão com paredes
        for (Bloco parede : paredes) {
            if (colisao(pacMan, parede)) {
                pacMan.x -= pacMan.velocidadeX;
                pacMan.y -= pacMan.velocidadeY;
                break;
            }
        }

        // Verifica colisão com fantasmas
        for (Bloco fantasma : fantasmas) {
            if (colisao(fantasma, pacMan)) {
                vidas--;
                if (vidas == 0) {
                    jogoTerminou = true;
                    return;
                }
                resetarPosicoes();
            }

            // Lógica de movimento dos fantasmas
            if (fantasma.y == tamanhoBloco * 9 && fantasma.direcao != 'C' && fantasma.direcao != 'B') {
                fantasma.atualizarDirecao('C');
            }
            fantasma.x += fantasma.velocidadeX;
            fantasma.y += fantasma.velocidadeY;
            for (Bloco parede : paredes) {
                if (colisao(fantasma, parede) || fantasma.x <= 0 || fantasma.x + fantasma.largura >= larguraTabuleiro) {
                    fantasma.x -= fantasma.velocidadeX;
                    fantasma.y -= fantasma.velocidadeY;
                    char novaDirecao = direcoes[random.nextInt(4)];
                    fantasma.atualizarDirecao(novaDirecao);
                }
            }
        }

        // Verifica se o Pac-Man comeu uma comida
        Bloco comidaRemovida = null;
        for (Bloco comida : comidas) {
            if (colisao(pacMan, comida)) {
                comidaRemovida = comida;
                pontuacao += 10;
            }
        }
        comidas.remove(comidaRemovida);

        // Se todas as comidas foram coletadas, reinicia o mapa
        if (comidas.isEmpty()) {
            carregarMapa();
            resetarPosicoes();
        }
    }

    // Verifica colisão entre dois blocos
    public boolean colisao(Bloco a, Bloco b) {
        return a.x < b.x + b.largura &&
               a.x + a.largura > b.x &&
               a.y < b.y + b.altura &&
               a.y + a.altura > b.y;
    }

    // Reseta as posições do Pac-Man e fantasmas
    public void resetarPosicoes() {
        pacMan.resetar();
        pacMan.velocidadeX = 0;
        pacMan.velocidadeY = 0;
        for (Bloco fantasma : fantasmas) {
            fantasma.resetar();
            char novaDirecao = direcoes[random.nextInt(4)];
            fantasma.atualizarDirecao(novaDirecao);
        }
    }

    // Chamado pelo timer para atualizar o jogo
    @Override
    public void actionPerformed(ActionEvent e) {
        mover();
        repaint();
        if (jogoTerminou) {
            loopJogo.stop();
        }
    }

    // Métodos não utilizados (mas obrigatórios por KeyListener)
    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {}

    // Trata teclas pressionadas
    @Override
    public void keyReleased(KeyEvent e) {
        // Reinicia o jogo se acabou
        if (jogoTerminou) {
            carregarMapa();
            resetarPosicoes();
            vidas = 3;
            pontuacao = 0;
            jogoTerminou = false;
            loopJogo.start();
        }

        // Movimenta o Pac-Man com as setas
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            pacMan.atualizarDirecao('C');
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            pacMan.atualizarDirecao('B');
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            pacMan.atualizarDirecao('E');
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            pacMan.atualizarDirecao('D');
        }

        // Atualiza a imagem do Pac-Man conforme a direção
        if (pacMan.direcao == 'C') {
            pacMan.imagem = imgPacManCima;
        } else if (pacMan.direcao == 'B') {
            pacMan.imagem = imgPacManBaixo;
        } else if (pacMan.direcao == 'E') {
            pacMan.imagem = imgPacManEsquerda;
        } else if (pacMan.direcao == 'D') {
            pacMan.imagem = imgPacManDireita;
        }
    }
}