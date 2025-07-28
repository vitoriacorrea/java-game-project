# **Pac-Man em Java**  

**Objetivo**:  
Este projeto foi desenvolvido como parte da disciplina de **Algoritmos (ALG)** do curso de **Análise e Desenvolvimento de Sistemas** no **IFSC - Instituto Federal de Santa Catarina, Campus São José**. O objetivo era criar/recriar um jogo clássico (Pac-Man) em **Java**, aplicando conceitos como programação orientada a objetos, estruturas de dados e manipulação de interfaces gráficas.  


## **📌 Sobre o Código**  

### **🔹 Tecnologias e Bibliotecas Utilizadas**  
- **Linguagem**: Java (JDK 8+)  
- **Interface Gráfica**: **Java Swing** (para renderização e janelas)  
- **Estruturas de Dados**: `HashSet` (para armazenar paredes, comidas e fantasmas)  
- **Tratamento de Inputs**: `KeyListener` (para capturar teclas do teclado)  
- **Game Loop**: `Timer` (para atualização e renderização do jogo)  

### **🔹 Funcionalidades Implementadas**  
✅ **Movimentação do Pac-Man** (setas do teclado: ↑, ↓, ←, →)  
✅ **Colisões com paredes e fantasmas**  
✅ **Sistema de pontuação** (coletar comidas = +10 pontos)  
✅ **Vidas do jogador** (3 vidas, game over ao perder todas)  
✅ **Geração aleatória de movimentos dos fantasmas**  
✅ **Reinício automático do jogo** (quando todas as comidas são coletadas ou game over)  



## **🎮 Como Executar**  

### **Pré-requisitos**  
- Java JDK 8+ instalado  
- Git (opcional, para clonar o repositório) 
- Você também pode instalar em ZIP e extrair no vscode 

### **Passos**  
1. **Clone o repositório** (ou baixe os arquivos `.java` e todas as imagens):  
   ```bash
   git clone https://github.com/vitoriacorrea/java-game-project.git
   cd pacman-java
   ```

2. **Compile e execute o jogo**:  
   ```bash
   javac App.java PacMan.java
   java App
   ```


## **🖥️ Interface Gráfica (Swing)**  

O jogo utiliza **Java Swing**, uma biblioteca gráfica padrão do Java, para:  
- Criar a janela do jogo (`JFrame`).  
- Renderizar sprites (`JPanel` + `Graphics`).  
- Gerenciar eventos de teclado (`KeyListener`).  

### **Pontos Importantes da Implementação**  
- **`Block` (Bloco)**: Classe interna que representa qualquer elemento do jogo (Pac-Man, fantasmas, paredes, comidas).  
- **`HashSet`**: Usado para armazenar e iterar eficientemente sobre paredes, comidas e fantasmas.  
- **Game Loop (`Timer`)**: Atualiza a lógica do jogo e redesenhos a cada **50ms (20 FPS)**.  
- **Colisões**: Verificadas manualmente (sem engines de física).  


## **📝 Observações Interessantes**  

- **Imagens Necessárias**: O jogo espera arquivos `.png` na mesma pasta (ex: `wall.png`, `blueGhost.png`, etc.).  
- **Melhorias Possíveis**:  
  - Adicionar menus (início, pausa).  
  - Implementar IA básica para os fantasmas.  
  - Usar JavaFX para gráficos mais fluidos.  
  - Corrigir bugs da parede
  - Implementar a cereja para fortalecimento do PacMan



## **🎓 Contexto Acadêmico**  

Este projeto foi desenvolvido como parte do curso de **Análise e Desenvolvimento de Sistemas** no **IFSC - São José**, com o intuito de aplicar conceitos de:  
- **Programação Orientada a Objetos (POO)** (classes, herança, polimorfismo).  
- **Estruturas de Dados** (coleções como `HashSet`).  
- **Manipulação de Interfaces Gráficas** (Swing).  



**👨‍💻 Alunas**: Ana Ronzani e Vitória Correa

**🏫 Instituição**: Instituto Federal de Santa Catarina - São José  
