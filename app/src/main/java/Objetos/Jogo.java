package Objetos;

import android.support.v7.app.AppCompatActivity;

import java.util.Arrays;
import java.util.Collections;
import java.util.Random;


public class Jogo extends AppCompatActivity {
    private Exercicio[] Exercicios;
    private int Dificuldade;
    private Random FuncRandom;
    int SegundosParaNovoExercicio = 5000;//5s
    private Thread ThreadAtualizaExercicios;

    public int getPontos() {
        return Pontos;
    }

    private int Pontos;

    public void Preparar(final int NumExercicios, final int Dificuldade) {
        Exercicios = new Exercicio[NumExercicios];

        for (int i = 0; i < NumExercicios;i++) {
            Exercicios[i] = new Exercicio();
            Exercicios[i].Preprar();
        }
        this.Dificuldade = Dificuldade;
        FuncRandom = new Random();
        PrepararThreadAtualizaExercicios();
    }

    private void PrepararThreadAtualizaExercicios() {
        ThreadAtualizaExercicios = new Thread() {
            public void run() {
                try {
                    do {
                        sleep(SegundosParaNovoExercicio);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                int Rand = FuncRandom.nextInt(Exercicios.length);
                                while (Exercicios[Rand].isAtivo() == true) {
                                    Rand = FuncRandom.nextInt(Exercicios.length);
                                }
                                Exercicios[Rand].ProximoExercicio(Dificuldade);
                            }
                        });
                    }
                    while (1 > 0);
                } catch (InterruptedException e) {
                }
            }
        };
        ThreadAtualizaExercicios.start();
    }

    public String getExercicio(int CodExercicio) {
        return Exercicios[CodExercicio].getTxtConta();
    }

    public Double getResultadoEsperado(int CodExercicio){
        return Exercicios[CodExercicio].getResultadoEsperado();
    }

    public int getPercentual(int CodExercicio) {
        return Exercicios[CodExercicio].getPercentualRestante();
    }

    public int getPeso(int CodExercicio) {
        return Exercicios[CodExercicio].getPeso();
    }

    public boolean isAtivo(int CodExercicio) {
        return Exercicios[CodExercicio].isAtivo();
    }

    public boolean ResolverExercicio(int CodExercicio, Double Resultado) {
        Double ResEsp = Exercicios[CodExercicio].getResultadoEsperado();
        Exercicios[CodExercicio].setResultado(Resultado);
        if (Exercicios[CodExercicio].getResultadoEsperado().equals(Exercicios[CodExercicio].getResultado())) {
            Pontos = Pontos + (Exercicios[CodExercicio].getPeso()+(Exercicios[CodExercicio].getPercentualRestante()/10));
            Exercicios[CodExercicio].ZerarExercicio();
            return true;
        } else {
            Pontos = Pontos - (Exercicios[CodExercicio].getPeso()+(Exercicios[CodExercicio].getPercentualRestante()/10));
            return false;
        }

    }

    public Double[] RetornaAlternativas(int CodExercicio) {
        Double ResEsp = Exercicios[CodExercicio].getResultadoEsperado();
        int MargemMin = FuncRandom.nextInt(10);
        int MargemMax = FuncRandom.nextInt(50);
        Double[] Opcoes = new Double[3];
        Opcoes[0] = ResEsp-(MargemMin*ResEsp/100);
        Opcoes[1] = ResEsp;
        Opcoes[2] = ResEsp+(MargemMax*ResEsp/100);
        Collections.shuffle(Arrays.asList(Opcoes));
        return Opcoes;
    }


}
