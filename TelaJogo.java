
import javax.sound.sampled.BooleanControl;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;
import java.math.*;


public class TelaJogo extends JPanel implements ActionListener {

    private static final int LARGURA_TELA = 1300;
    private static final int ALTURA_TELA = 750;
    private static final int TAMANHO_BLOCO = 50;
    private static final int UNIDADES = LARGURA_TELA * ALTURA_TELA / (TAMANHO_BLOCO * TAMANHO_BLOCO);
    private  double INTERVALO = 400;
    private static final String NOME_FONTE = "Ink Free";
    private final int[] eixoX = new int[UNIDADES];
    private final int[] eixoY = new int[UNIDADES];
    private int corpoCobra = 6;
    private int blocosComidos;
    private int blocoX;
    private int blocoY;
    private char direcao = 'D'; // C - Cima, B - Baixo, E - Esquerda, D - Direita
    private boolean estaRodando = false;
    private double tempoInicial = 400;
    private double coeficienteAngular = 0;
    private Boolean estaRodando1 = true;
    private long cosseno = 0;
    //private int contador = 0;
    Timer timer;
    Random random;

    TelaJogo() {
        random = new Random();
        setPreferredSize(new Dimension(LARGURA_TELA, ALTURA_TELA));
        setBackground(Color.black);
        setFocusable(true);
        //addKeyListener(new );
        iniciarJogo();
    }

    public void iniciarJogo() {
        criarBloco();
        estaRodando = true;
        timer = new Timer((int)getIntervalo(), this);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        desenharTela(g);
    }

    public void desenharTela(Graphics g) {

        if (estaRodando) {
            g.setColor(Color.red);
            g.fillOval(blocoX, blocoY, TAMANHO_BLOCO, TAMANHO_BLOCO);

            for (int i = 0; i < corpoCobra; i++) {
                if (i == 0) {
                    g.setColor(Color.blue);
                    g.drawLine(eixoX[0],eixoY[0], blocoX, blocoY); // Desenha a linha da cabeça
                    coeficienteAngular = (double)(eixoY[0] - ALTURA_TELA) / (eixoX[0] - LARGURA_TELA);
                    if(Math.tan(coeficienteAngular) != 0){
                        g.drawLine(eixoX[0], eixoY[0], ALTURA_TELA, LARGURA_TELA);
                    }     
                    else{
                        g.drawLine(eixoX[0], eixoY[0], ALTURA_TELA, LARGURA_TELA);
                    }  
                    g.fillRect(eixoX[0], eixoY[0], TAMANHO_BLOCO, TAMANHO_BLOCO);
                } else {
                    g.setColor(Color.white);
                    g.fillRect(eixoX[i], eixoY[i], TAMANHO_BLOCO, TAMANHO_BLOCO);
                }
            }
            g.setColor(Color.yellow);
            g.setFont(new Font(NOME_FONTE, Font.BOLD, 40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Pontos: " + getBlocos(), (LARGURA_TELA - metrics.stringWidth("Pontos: " + getBlocos())) / 2, g.getFont().getSize());
            g.drawString("Tempo" + tempoInicial,  g.getFont().getSize() , (ALTURA_TELA - metrics.stringWidth("Tempo" + tempoInicial)) / 2);
        } else {
            fimDeJogo(g);
        }
    }

    public char sensorCabeca() {
        // Calcula o coeficiente angular apenas se o denominador não for zero

    
            // Verifica a posição relativa da cabeça em relação ao eixo X e Y
            for(int i =0; i < corpoCobra ; i++){
                if (blocoX > eixoX[0] && eixoX[0] != eixoX[i] && eixoY[i] != eixoY[0]) {
                    direcao = 'D';
                } else if (blocoX < eixoX[0] && eixoX[0] != eixoX[i] && eixoY[i] != eixoY[0]) {
                    direcao = 'E';
                } else {
                    if (blocoY > eixoY[0] && eixoX[0] != eixoX[i] && eixoY[i] != eixoY[0]) {
                        direcao = 'B';
                    } else {
                        direcao = 'C';
                    }
                }
            }
           
            return direcao;
        }
    
      
    
    
    

    

    private void setIntervalo(double intervalo) {
        this.INTERVALO = intervalo;
    }
        

    private double getIntervalo() {
        return INTERVALO;
    } 

    private int getBlocos(){
        return blocosComidos;
    }

    private void setBlocos(int blocosComidos){
        this.blocosComidos = blocosComidos;
    }

    private void criarBloco() {
        blocoX = random.nextInt(LARGURA_TELA / TAMANHO_BLOCO) * TAMANHO_BLOCO;
        blocoY = random.nextInt(ALTURA_TELA / TAMANHO_BLOCO) * TAMANHO_BLOCO;
    }

    public void fimDeJogo(Graphics g) {
        g.setColor(Color.red);
        g.setFont(new Font(NOME_FONTE, Font.BOLD, 40));
        FontMetrics fontePontuacao = getFontMetrics(g.getFont());
        g.drawString("Pontos: " + blocosComidos, (LARGURA_TELA - fontePontuacao.stringWidth("Pontos: " + blocosComidos)) / 2, g.getFont().getSize());
        g.setColor(Color.red);
        g.setFont(new Font(NOME_FONTE, Font.BOLD, 75));
        FontMetrics fonteFinal = getFontMetrics(g.getFont());
        g.drawString("\uD83D\uDE1D Fim do Jogo.", (LARGURA_TELA - fonteFinal.stringWidth("Fim do Jogo")) / 2, ALTURA_TELA / 2);
    }

    public void actionPerformed(ActionEvent e) {
        if (estaRodando) {
            atualizarDirecaoAutomaticamente();
            andar();
            alcancarBloco();
            //validarLimites();
            
        }
        repaint();
    }

   

    private void alcancarBloco() {
        if (eixoX[0] == blocoX && eixoY[0] == blocoY) {
            corpoCobra++;
            setBlocos(blocosComidos + 1);
            criarBloco();
            aumentarVelocidade(getBlocos());
        }
    }

    private void aumentarVelocidade(int blocos){
            if (getBlocos()!= 0 &&  getBlocos() % 5 == 0) {
                tempoInicial = (getIntervalo() * 0.8);
                setIntervalo(tempoInicial);
                timer.setDelay((int) tempoInicial);
            }
        } 


    private boolean bateuCorpo(){
        for (int i = corpoCobra; i > 0; i--) {
            if (eixoX[0] == eixoX[i] && eixoY[0] == eixoY[i]) {
                estaRodando1 = false;
            }
        }
        return estaRodando1;
    }
        
        
    private boolean validarLimites2() {
            //A cabeça bateu no corpo?
            for (int i = corpoCobra; i > 0; i--) {
                if (eixoX[0] == eixoX[i] && eixoY[0] == eixoY[i]) {
                    estaRodando1 = false;
                }
            }
    
            //A cabeça tocou uma das bordas Direita ou esquerda?
            if (eixoX[0] < 0 || eixoX[0] > LARGURA_TELA) {
                estaRodando1 = false;
            }
    
            //A cabeça tocou o piso ou o teto?
            if (eixoY[0] < 0 || eixoY[0] > ALTURA_TELA) {
                estaRodando1 = false;
            }
            return estaRodando1;
        }

    private void validarLimites() {
        //A cabeça bateu no corpo?
        for (int i = corpoCobra; i > 0; i--) {
            if (eixoX[0] == eixoX[i] && eixoY[0] == eixoY[i]) {
                estaRodando = true ;
                break;
            }
        }

        //A cabeça tocou uma das bordas Direita ou esquerda?
        if (eixoX[0] < 0 || eixoX[0] > LARGURA_TELA) {
            estaRodando = false;
        }

        //A cabeça tocou o piso ou o teto?
        if (eixoY[0] < 0 || eixoY[0] > ALTURA_TELA) {
            estaRodando = false;
        }

        if (!estaRodando) {
            timer.stop();
        }
    }


    
    private void andar() {
        for (int i = corpoCobra; i > 0; i--) {
            eixoX[i] = eixoX[i - 1];
            eixoY[i] = eixoY[i - 1];
        }

        switch (direcao) {
            case 'C':
                eixoY[0] = eixoY[0] - TAMANHO_BLOCO;
                break;
            case 'B':
                eixoY[0] = eixoY[0] + TAMANHO_BLOCO;
                break;
            case 'E':
                eixoX[0] = eixoX[0] - TAMANHO_BLOCO;
                break;
            case 'D':
                eixoX[0] = eixoX[0] + TAMANHO_BLOCO;
                break;
            default:
                break;
        }
    }

    public class LeitorDeTeclasAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            atualizarDirecaoAutomaticamente();
        }
    }
        /*public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (direcao != 'D') {
                        direcao = 'E';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direcao != 'E') {
                        direcao = 'D';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (direcao != 'B') {
                        direcao = 'C';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (direcao != 'C') {
                        direcao = 'B';
                    }
                    break;
                default:
                    break;
            }
        }
    }*/



   private int getCabecaX() {
        return eixoX[0];
    }

    private int getCabecaY() {
        return eixoY[0];
    }

    private int getComidaX() {
        return blocoX;
    }

    private int getComidaY() {
        return blocoY;
    }




    private char calcularDirecaoComida() {
        // Obtenha as coordenadas da cabeça da cobra
        int cabecaX = getCabecaX();
        int cabecaY = getCabecaY();
    
        // Obtenha as coordenadas da comida
        int comidaX = getComidaX();
        int comidaY = getComidaY();
    
        // Calcule a diferença entre as coordenadas da comida e da cabeça da cobra
        int difX = comidaX - cabecaX;
        int difY = comidaY - cabecaY;
    
        // Verifique a direção da comida em relação à cabeça da cobra
        if (Math.abs(difX) > Math.abs(difY)) {
            // A comida está mais distante na direção horizontal (esquerda ou direita)
            if (difX > 0) {
                // A comida está à direita da cabeça da cobra
                return 'D';
            } else {
                // A comida está à esquerda da cabeça da cobra
                return 'E';
            }
        } else {
            // A comida está mais distante na direção vertical (cima ou baixo)
            if (difY > 0  ) {
                // A comida está abaixo da cabeça da cobra
                return 'B';
            } else {
                // A comida está acima da cabeça da cobra
                return 'C';
            }
        }
    }
    


    private void atualizarDirecaoAutomaticamente() {
        // Obtenha a direção atual da cabeça da cobra
        char direcaoAtual = sensorCabeca();
        if(direcaoAtual == 'N'){
            return;
        }
    
        // Verifique se a cobra pode se mover na direção atual sem bater na parede
        if (!validarLimites2() && !bateuCorpo()) {
            // Calcule a direção para a comida
            char direcaoComida = calcularDirecaoComida();
            
            // Verifique se a cobra pode se mover em direção à comida sem bater na parede
            if (direcaoComida != 'N' && !validarLimites2() && !bateuCorpo()) {
                // Atualize a direção da cobra para a direção da comida
                direcao = direcaoComida;
            } else {
                // Se não puder se mover em direção à comida, escolha uma direção aleatória
                int novaDirecao = (int) (Math.random() * 4); // 0: C, 1: B, 2: E, 3: D
    
                // Atualize a direção da cobra, exceto se for uma direção oposta à direção atual
                switch (novaDirecao) {
                    case 0:
                        if (direcaoAtual != 'B') {
                            direcao = 'C';
                        }
                        break;
                    case 1:
                        if (direcaoAtual != 'C') {
                            direcao = 'B';
                        }
                        break;
                    case 2:
                        if (direcaoAtual != 'D') {
                            direcao = 'E';
                        }
                        break;
                    case 3:
                        if (direcaoAtual != 'E') {
                            direcao = 'D';
                        }
                        break;
                    default:
                        break;
                }
            }
        } else {
            // Se a cobra estiver prestes a bater na parede, escolha uma direção aleatória
            int novaDirecao = (int) (Math.random() * 4); // 0: C, 1: B, 2: E, 3: D
    
            // Atualize a direção da cobra, exceto se for uma direção oposta à direção atual
            switch (novaDirecao) {
                case 0:
                    if (direcaoAtual != 'B') {
                        direcao = 'C';
                    }
                    break;
                case 1:
                    if (direcaoAtual != 'C') {
                        direcao = 'B';
                    }
                    break;
                case 2:
                    if (direcaoAtual != 'D') {
                        direcao = 'E';
                    }
                    break;
                case 3:
                    if (direcaoAtual != 'E') {
                        direcao = 'D';
                    }
                    break;
                default:
                    break;
            }
        }
    }
    




}

  
