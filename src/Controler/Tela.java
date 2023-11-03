package Controler;

import Fases.Fase;
import Modelo.Personagem;
import Modelo.Octorok;
import Modelo.Hero;
import Modelo.Estatico;
import Modelo.BichinhoVaiVemHorizontal;
import Auxiliar.Consts;
import Auxiliar.Desenho;
import Modelo.ZigueZague;
import auxiliar.Posicao;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import javax.swing.JButton;

public class Tela extends javax.swing.JFrame implements MouseListener, KeyListener {

    private Hero hero;
    private ArrayList<Personagem> faseAtual;
    private ControleDeJogo cj = new ControleDeJogo();
    private Graphics g2;
    private int constDelay = 1;
    private int idelay = constDelay;
    private int nivel = 0;

    public Tela() {
        Desenho.setCenario(this);
        initComponents();
        this.addMouseListener(this);
        /*mouse*/

        this.addKeyListener(this);
        /*teclado*/
 /*Cria a janela do tamanho do tabuleiro + insets (bordas) da janela*/
        this.setSize(Consts.RES * Consts.CELL_SIDE + getInsets().left + getInsets().right, (Consts.RES - 4) * Consts.CELL_SIDE + getInsets().top + getInsets().bottom);

        faseAtual = new ArrayList<Personagem>();

        /*Cria faseAtual adiciona personagens*/
        hero = new Hero("linkDown.png");
        this.addPersonagem(hero);
    }

    public void addInimigos() {
        BichinhoVaiVemHorizontal bBichinhoH = new BichinhoVaiVemHorizontal("octorok.png");
        bBichinhoH.setPosicao(8, 5);
        this.addPersonagem(bBichinhoH);

        Octorok bV = new Octorok("octorok.png");
        bV.setPosicao(9, 5);
        this.addPersonagem(bV);
    }

    public boolean ehPosicaoValida(Posicao p) {
        return cj.ehPosicaoValida(this.faseAtual, p);
    }

    public void addPersonagem(Personagem umPersonagem) {
        faseAtual.add(umPersonagem);
    }

    public void removePersonagem(Personagem umPersonagem) {
        faseAtual.remove(umPersonagem);
    }

    public Graphics getGraphicsBuffer() {
        return g2;
    }

    public void paint(Graphics gOld) {
        int cont_aux = 1;
        Graphics g = this.getBufferStrategy().getDrawGraphics();
        /*Criamos um contexto gráfico*/
        g2 = g.create(getInsets().left, getInsets().top, getWidth() - getInsets().right, getHeight() - getInsets().top);
        /**
         * ***********Desenha cenário de fundo*************
         */
        for (int i = 0; i < Consts.RES; i++) {
            for (int j = 0; j < Consts.RES; j++) {
                try {
                    Image newImage = Toolkit.getDefaultToolkit().getImage(new java.io.File(".").getCanonicalPath() + Consts.PATH + "areia.png");
                    g2.drawImage(newImage, j * Consts.CELL_SIDE, i * Consts.CELL_SIDE, Consts.CELL_SIDE, Consts.CELL_SIDE, null);
                } catch (IOException ex) {
                    Logger.getLogger(Tela.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        if (!this.faseAtual.isEmpty()) { 
        if(nivel == 0){ // Tentando criar o primeiro nivel
        Fase fase = new Fase();
        ArrayList<Personagem> f = new ArrayList<Personagem>();
        f = fase.criaFase(0, null, faseAtual);
        faseAtual = f;
        nivel++;
        }
        
            this.cj.desenhaTudo(faseAtual);
            try {
                this.cj.processaTudo(faseAtual);
                if (idelay == 2 && hero.getVida() <= 2 && hero.getTemEspada()){
                    if ((faseAtual.get(faseAtual.size()-1)).getsIsProjetil()){
                        Desenho.acessoATelaDoJogo().removePersonagem(faseAtual.get(faseAtual.size()-1));
                    }
                    hero.setTemEspada(false);
                    faseAtual.get(7).setDesenho("espadaHUD.png");
                }
                if (idelay == 4 && hero.getVida() == 3){
                    hero.setTemEspada(false);
                    faseAtual.get(7).setDesenho("espadaHUD.png");
                }
            } catch (IOException ex) {
                Logger.getLogger(Tela.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        g.dispose();
        g2.dispose();
        if (!getBufferStrategy().contentsLost()) {
            getBufferStrategy().show();
        }
        if (idelay < 20) {
            idelay++;
        }
    }
    }

    public void go() {
        TimerTask task = new TimerTask() {
            public void run() {
                repaint();
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 0, Consts.PERIOD);
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_A) {
            try {
                if (hero.getTemEspada() == false){
                    if (hero.getVida() == 3){
                        hero.espada(hero.getOlhando(), 0);
                    } else{
                        hero.espada(hero.getOlhando(), 1);
                    }
                    faseAtual.get(7).setDesenho("espadaHUDCinza.png");
                }
                idelay = constDelay;
                
            } catch (IOException ex) {
                Logger.getLogger(Tela.class.getName()).log(Level.SEVERE, null, ex);
            }
            hero.setTemEspada(true);
        }/* else if (e.getKeyCode() == KeyEvent.VK_F) {
            faseCentral = faseAtual;
            ArrayList<Personagem> fase = new ArrayList<Personagem>();
            fase.add(hero);
            Octorok bV = new Octorok("octorok.png");
            bV.setPosicao(9, 3);
            fase.add(bV);
            faseAtual = fase;
        } else if (e.getKeyCode() == KeyEvent.VK_G) {
            faseAtual = faseCentral;
        }*/ else if (e.getKeyCode() == KeyEvent.VK_UP && idelay >= constDelay) {
            idelay = 0;
            hero.setOlhando(0);
            hero.moveUp();
            if (hero.getTemEspada() && hero.getVida() <= 2 && (faseAtual.get(faseAtual.size()-1)).getsIsProjetil()){
                Desenho.acessoATelaDoJogo().removePersonagem(faseAtual.get(faseAtual.size()-1));
                hero.setTemEspada(false);
            }
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN && idelay >= constDelay) {
            idelay = 0;
            hero.setOlhando(2);
            hero.moveDown();
            if (hero.getTemEspada() && hero.getVida() <= 2 && (faseAtual.get(faseAtual.size()-1)).getsIsProjetil()){
                Desenho.acessoATelaDoJogo().removePersonagem(faseAtual.get(faseAtual.size()-1));
                hero.setTemEspada(false);
            }
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT && idelay >= constDelay) {
            idelay = 0;
            hero.setOlhando(3);
            hero.moveLeft();
            if (hero.getTemEspada() && hero.getVida() <= 2 && (faseAtual.get(faseAtual.size()-1)).getsIsProjetil()){
                Desenho.acessoATelaDoJogo().removePersonagem(faseAtual.get(faseAtual.size()-1));
                hero.setTemEspada(false);
            }
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT && idelay >= constDelay) {
            idelay = 0;
            hero.setOlhando(1);
            hero.moveRight();
            if (hero.getTemEspada() && hero.getVida() <= 2 && (faseAtual.get(faseAtual.size()-1)).getsIsProjetil()){
                Desenho.acessoATelaDoJogo().removePersonagem(faseAtual.get(faseAtual.size()-1));
                hero.setTemEspada(false);
            }
        }
        this.setTitle("-> Cell: " + (hero.getPosicao().getColuna()) + ", " + (hero.getPosicao().getLinha()));
    }
    //repaint(); /*invoca o paint imediatamente, sem aguardar o refresh*/

    public void mousePressed(MouseEvent e) {
        /* Clique do mouse desligado*/
        int x = e.getX();
        int y = e.getY();

        this.setTitle("X: " + x + ", Y: " + y
                + " -> Cell: " + (y / Consts.CELL_SIDE) + ", " + (x / Consts.CELL_SIDE));

        this.hero.getPosicao().setPosicao(y / Consts.CELL_SIDE, x / Consts.CELL_SIDE);

        repaint();
    }


    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("POO2023-1 - Skooter");
        setAlwaysOnTop(true);
        setAutoRequestFocus(false);
        setResizable(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 561, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 500, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

    public void mouseMoved(MouseEvent e) {
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseDragged(MouseEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
    }
}
