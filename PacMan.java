import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Random;
import javax.swing.*;
// Classe principal que herda o Jpanel e implementa a logica grafica, o controle do teclado e o loop do jogo
public class PacMan extends JPanel implements ActionListener, KeyListener {
    // Classe interna para representar qualquer bloco/entidade do jogo
    class Bloco {
        int x;              // Posição X no tabuleiro
        int y;              // Posição Y no tabuleiro
        int largura;        // Largura do bloco
        int altura;         // Altura do bloco
        Image imagem;       // Imagem do bloco

        int posicaoInicialX;   // Posição X inicial (para reset)
        int posicaoInicialY;    // Posição Y inicial (para reset)
        char direcao = 'U';     // Direção atual (U=Up/Cima, D=Down/Baixo, L=Left/Esquerda, R=Right/Direita)
        int velocidadeX = 0;    // Velocidade no eixo X
        int velocidadeY = 0;    // Velocidade no eixo Y

        Bloco(Image imagem, int x, int y, int largura, int altura) {
            this.imagem = imagem;
            this.x = x;
            this.y = y;
            this.largura = largura;
            this.altura = altura;
            this.posicaoInicialX = x;
            this.posicaoInicialY = y;
        }

        // Atualiza a direção do bloco
        void atualizarDirecao(char direcao) {
            char direcaoAnterior = this.direcao;
            this.direcao = direcao;
            atualizarVelocidade();
            this.x += this.velocidadeX;
            this.y += this.velocidadeY;
            
            // Verifica colisão com paredes após mover
            for (Bloco parede : paredes) {
                if (colisao(this, parede)) {
                    // Desfaz o movimento se colidir
                    this.x -= this.velocidadeX;
                    this.y -= this.velocidadeY;
                    // Volta para direção anterior
                    this.direcao = direcaoAnterior;
                    atualizarVelocidade();
                }
            }
        }

        // Atualiza a velocidade baseada na direção
        void atualizarVelocidade() {
            if (this.direcao == 'U') {
                this.velocidadeX = 0;
                this.velocidadeY = -tamanhoBloco/4;  // Move para cima
            }
            else if (this.direcao == 'D') {
                this.velocidadeX = 0;
                this.velocidadeY = tamanhoBloco/4;   // Move para baixo
            }
            else if (this.direcao == 'L') {
                this.velocidadeX = -tamanhoBloco/4;  // Move para esquerda
                this.velocidadeY = 0;
            }
            else if (this.direcao == 'R') {
                this.velocidadeX = tamanhoBloco/4;   // Move para direita
                this.velocidadeY = 0;
            }
        }

        // Reseta o bloco para sua posição inicial
        void resetar() {
            this.x = this.posicaoInicialX;
            this.y = this.posicaoInicialY;
        }
    }

    // Configurações do jogo
    private int quantidadeLinhas = 21;       // Número de linhas no mapa
    private int quantidadeColunas = 19;      // Número de colunas no mapa
    private int tamanhoBloco = 32;           // Tamanho de cada bloco em pixels
    private int larguraTabuleiro = quantidadeColunas * tamanhoBloco;  // Largura total do tabuleiro
    private int alturaTabuleiro = quantidadeLinhas * tamanhoBloco;    // Altura total do tabuleiro

    // Imagens do jogo
    private Image imagemParede;              // Imagem da parede
    private Image imagemFantasmaAzul;        // Imagem do fantasma azul
    private Image imagemFantasmaLaranja;     // Imagem do fantasma laranja
    private Image imagemFantasmaRosa;        // Imagem do fantasma rosa
    private Image imagemFantasmaVermelho;    // Imagem do fantasma vermelho

    private Image imagemPacmanCima;          // Imagem do Pac-Man virado para cima
    private Image imagemPacmanBaixo;         // Imagem do Pac-Man virado para baixo
    private Image imagemPacmanEsquerda;      // Imagem do Pac-Man virado para esquerda
    private Image imagemPacmanDireita;       // Imagem do Pac-Man virado para direita

    // Mapa do jogo baseado em um mapa de texto onde cada caractere representa um elemento (X = parede, O = vazio, P = Pac-Man, ' ' = comida)
    // Fantasmas: b = azul, o = laranja, p = rosa, r = vermelho
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
    HashSet<Bloco> paredes;     // Paredes do labirinto
    HashSet<Bloco> comidas;     // Pontos/comidas
    HashSet<Bloco> fantasmas;   // Fantasmas
    Bloco pacman;               // Personagem principal

    Timer loopJogo;             // Timer para o loop do jogo
    char[] direcoes = {'U', 'D', 'L', 'R'};  // Direções possíveis
    Random random = new Random();  // Gerador de números aleatórios
    int pontuacao = 0;          // Pontuação do jogador
    int vidas = 3;              // Número de vidas
    boolean jogoTerminado = false;  // Estado do jogo

    // Construtor define o tamanho da tela, carrega imagens dos personagens, inicia o timer
    PacMan() {
        setPreferredSize(new Dimension(larguraTabuleiro, alturaTabuleiro));
        setBackground(Color.BLACK);
        addKeyListener(this);
        setFocusable(true);

        // Carrega as imagens
        imagemParede = new ImageIcon(getClass().getResource("./wall.png")).getImage();
        imagemFantasmaAzul = new ImageIcon(getClass().getResource("./blueGhost.png")).getImage();
        imagemFantasmaLaranja = new ImageIcon(getClass().getResource("./orangeGhost.png")).getImage();
        imagemFantasmaRosa = new ImageIcon(getClass().getResource("./pinkGhost.png")).getImage();
        imagemFantasmaVermelho = new ImageIcon(getClass().getResource("./redGhost.png")).getImage();

        imagemPacmanCima = new ImageIcon(getClass().getResource("./pacmanUp.png")).getImage();
        imagemPacmanBaixo = new ImageIcon(getClass().getResource("./pacmanDown.png")).getImage();
        imagemPacmanEsquerda = new ImageIcon(getClass().getResource("./pacmanLeft.png")).getImage();
        imagemPacmanDireita = new ImageIcon(getClass().getResource("./pacmanRight.png")).getImage();

        carregarMapa();
        
        // Define direções aleatórias para os fantasmas
        for (Bloco fantasma : fantasmas) {
            char novaDirecao = direcoes[random.nextInt(4)];
            fantasma.atualizarDirecao(novaDirecao);
        }
        
        // Configura o timer do jogo (50ms = 20fps)
        loopJogo = new Timer(50, this);
        loopJogo.start();
    }

    // Carrega o mapa e cria os objetos do jogo
    public void carregarMapa() {
        paredes = new HashSet<Bloco>();
        comidas = new HashSet<Bloco>();
        fantasmas = new HashSet<Bloco>();

        for (int linha = 0; linha < quantidadeLinhas; linha++) {
            for (int coluna = 0; coluna < quantidadeColunas; coluna++) {
                String linhaMapa = mapa[linha];
                char caractere = linhaMapa.charAt(coluna);

                int x = coluna * tamanhoBloco;
                int y = linha * tamanhoBloco;

                if (caractere == 'X') { // Parede
                    Bloco parede = new Bloco(imagemParede, x, y, tamanhoBloco, tamanhoBloco);
                    paredes.add(parede);
                }
                else if (caractere == 'b') { // Fantasma azul
                    Bloco fantasma = new Bloco(imagemFantasmaAzul, x, y, tamanhoBloco, tamanhoBloco);
                    fantasmas.add(fantasma);
                }
                else if (caractere == 'o') { // Fantasma laranja
                    Bloco fantasma = new Bloco(imagemFantasmaLaranja, x, y, tamanhoBloco, tamanhoBloco);
                    fantasmas.add(fantasma);
                }
                else if (caractere == 'p') { // Fantasma rosa
                    Bloco fantasma = new Bloco(imagemFantasmaRosa, x, y, tamanhoBloco, tamanhoBloco);
                    fantasmas.add(fantasma);
                }
                else if (caractere == 'r') { // Fantasma vermelho
                    Bloco fantasma = new Bloco(imagemFantasmaVermelho, x, y, tamanhoBloco, tamanhoBloco);
                    fantasmas.add(fantasma);
                }
                else if (caractere == 'P') { // Pac-Man
                    pacman = new Bloco(imagemPacmanDireita, x, y, tamanhoBloco, tamanhoBloco);
                }
                else if (caractere == ' ') { // Comida
                    Bloco comida = new Bloco(null, x + 14, y + 14, 4, 4);
                    comidas.add(comida);
                }
            }
        }
    }
    // Desenha as imagens do pacman, fantasmas e paredes
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        desenhar(g);
    }

    // Desenha todos os elementos do jogo
    public void desenhar(Graphics g) {
        // Desenha o Pac-Man
        g.drawImage(pacman.imagem, pacman.x, pacman.y, pacman.largura, pacman.altura, null);

        // Desenha os fantasmas
        for (Bloco fantasma : fantasmas) {
            g.drawImage(fantasma.imagem, fantasma.x, fantasma.y, fantasma.largura, fantasma.altura, null);
        }

        // Desenha as paredes
        for (Bloco parede : paredes) {
            g.drawImage(parede.imagem, parede.x, parede.y, parede.largura, parede.altura, null);
        }

        // Desenha as comidas (pontos pequenos)
        g.setColor(Color.WHITE);
        for (Bloco comida : comidas) {
            g.fillRect(comida.x, comida.y, comida.largura, comida.altura);
        }
        
        // Desenha a pontuação e vidas
        g.setFont(new Font("Arial", Font.PLAIN, 18));
        if (jogoTerminado) {
            g.drawString("Fim de Jogo: " + String.valueOf(pontuacao), tamanhoBloco/2, tamanhoBloco/2);
        }
        else {
            g.drawString("x" + String.valueOf(vidas) + " Pontos: " + String.valueOf(pontuacao), tamanhoBloco/2, tamanhoBloco/2);
        }
    }

    // Atualiza a lógica de movimentação do jogo, anda conforme a tecla pressionada, é bloqueado por paredes, se encostar em um fantasma perde vida
    public void mover() {
        // Move o Pac-Man
        pacman.x += pacman.velocidadeX;
        pacman.y += pacman.velocidadeY;

        // Verifica se o Pac-Man saiu completamente do mapa pelos lados
        if (pacman.x + pacman.largura < 0) {
            // Saiu pela esquerda - aparece na direita
            pacman.x = larguraTabuleiro;
        } else if (pacman.x > larguraTabuleiro) {
            // Saiu pela direita - aparece na esquerda
            pacman.x = -pacman.largura;
        }

        // Verifica se o Pac-Man saiu completamente do mapa por cima ou por baixo
        if (pacman.y + pacman.altura < 0) {
            // Saiu por cima - aparece em baixo
            pacman.y = alturaTabuleiro;
        } else if (pacman.y > alturaTabuleiro) {
            // Saiu por baixo - aparece em cima
            pacman.y = -pacman.altura;
        }
        // ==============================================

        // Verifica colisão com paredes (apenas depois de verificar os limites do mapa)
        for (Bloco parede : paredes) {
            if (colisao(pacman, parede)) {
                // Desfaz o movimento se colidir com parede
                pacman.x -= pacman.velocidadeX;
                pacman.y -= pacman.velocidadeY;
                break;
            }
        }

        // Verifica colisão com fantasmas
        for (Bloco fantasma : fantasmas) {
            if (colisao(fantasma, pacman)) {
                vidas -= 1;
                if (vidas == 0) {
                    jogoTerminado = true;
                    return;
                }
                resetarPosicoes();
            }

            // Lógica de movimento dos fantasmas
            if (fantasma.y == tamanhoBloco*9 && fantasma.direcao != 'U' && fantasma.direcao != 'D') {
                fantasma.atualizarDirecao('U');
            }
            fantasma.x += fantasma.velocidadeX;
            fantasma.y += fantasma.velocidadeY;
            for (Bloco parede : paredes) {
                if (colisao(fantasma, parede) || fantasma.x <= 0 || fantasma.x + fantasma.largura >= larguraTabuleiro) {
                    // Fantasma colidiu com parede - muda de direção
                    fantasma.x -= fantasma.velocidadeX;
                    fantasma.y -= fantasma.velocidadeY;
                    char novaDirecao = direcoes[random.nextInt(4)];
                    fantasma.atualizarDirecao(novaDirecao);
                }
            }
        }

        // Verifica se comeu comida
        Bloco comidaComida = null;
        for (Bloco comida : comidas) {
            if (colisao(pacman, comida)) {
                comidaComida = comida;
                pontuacao += 10;
            }
        }
        comidas.remove(comidaComida);

        // Se todas as comidas foram comidas, recarrega o mapa
        if (comidas.isEmpty()) {
            carregarMapa();
            resetarPosicoes();
        }
    }

    // Verifica colisão entre dois blocos
    public boolean colisao(Bloco a, Bloco b) {
        return  a.x < b.x + b.largura &&
                a.x + a.largura > b.x &&
                a.y < b.y + b.altura &&
                a.y + a.altura > b.y;
    }

    // Reseta as posições dos personagens
    public void resetarPosicoes() {
        pacman.resetar();
        pacman.velocidadeX = 0;
        pacman.velocidadeY = 0;
        for (Bloco fantasma : fantasmas) {
            fantasma.resetar();
            char novaDirecao = direcoes[random.nextInt(4)];
            fantasma.atualizarDirecao(novaDirecao);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        mover();
        repaint();
        if (jogoTerminado) {
            loopJogo.stop();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {
        // Reinicia o jogo se terminado e uma tecla for pressionada
        if (jogoTerminado) {
            carregarMapa();
            resetarPosicoes();
            vidas = 3;
            pontuacao = 0;
            jogoTerminado = false;
            loopJogo.start();
        }
        
        // Controles do Pac-Man
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            pacman.atualizarDirecao('U');
        }
        else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            pacman.atualizarDirecao('D');
        }
        else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            pacman.atualizarDirecao('L');
        }
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            pacman.atualizarDirecao('R');
        }

        // Atualiza a imagem do Pac-Man conforme a direção
        if (pacman.direcao == 'U') {
            pacman.imagem = imagemPacmanCima;
        }
        else if (pacman.direcao == 'D') {
            pacman.imagem = imagemPacmanBaixo;
        }
        else if (pacman.direcao == 'L') {
            pacman.imagem = imagemPacmanEsquerda;
        }
        else if (pacman.direcao == 'R') {
            pacman.imagem = imagemPacmanDireita;
        }
    }
}
