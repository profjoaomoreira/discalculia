package com.joao.calculei;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class TelaJogo extends AppCompatActivity {

    private GridView gvJogo;
    private ArrayAdapter<String> gvDadosJogo;
    Objetos.Jogo ObjJogo;
    int ExerciciosEmTela = 30;
    int NivelDeDificuldade = 3;
    private Thread ThreadAtualizaGrid;
    Button btnDesistir;
    Button btnOpcao1;
    Button btnOpcao2;
    Button btnOpcao3;
    TextView txtConta;
    TextView txtPontos;
    int CodExec = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_tela_jogo);

        gvJogo = (GridView) findViewById(R.id.gvJogo);
        btnDesistir = (Button) findViewById(R.id.btnDesistir);
        txtConta = (TextView) findViewById(R.id.txtConta);
        txtPontos= (TextView) findViewById(R.id.txtPontos);

        btnDesistir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Banco.BDAdapter.executarComandoSQL(TelaJogo.this,"insert into ranking(nome, pontuacao) values('teste', '"+ObjJogo.getPontos()+"')");
                finish();
            }
        });

        PrepararSetorResultado();
        PrepararComponentesGrid();
        PrepararAtualizacaoGrid();
    }

    private void PrepararAtualizacaoGrid(){
        ThreadAtualizaGrid = new Thread() { //new thread
            public void run() {
                try {
                    do {
                        sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (gvDadosJogo.getCount() > 0) {
                                    gvDadosJogo.clear();
                                }
                                gvDadosJogo.clear();
                                for (int i = 0;i<ExerciciosEmTela;i++) {
                                    gvDadosJogo.add(String.valueOf(ObjJogo.getPercentual(i)));
                                }
                            }
                        });


                    }
                    while (1 > 0);
                } catch (InterruptedException e) {

                }
            }
        };
        ThreadAtualizaGrid.start();
    }

    private void PrepararSetorResultado() {
        btnOpcao1 =(Button) findViewById(R.id.btnOpcao1);
        btnOpcao2 =(Button) findViewById(R.id.btnOpcao2);
        btnOpcao3 =(Button) findViewById(R.id.btnOpcao3);

        setInvisibleAlternativas();
        btnOpcao1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VerificarResultado(Double.parseDouble(btnOpcao1.getText().toString()));
            }
        });
        btnOpcao2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VerificarResultado(Double.parseDouble(btnOpcao2.getText().toString()));
            }
        });
        btnOpcao3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VerificarResultado(Double.parseDouble(btnOpcao3.getText().toString()));
            }
        });
    }

    private void VerificarResultado(Double ResultadoClick) {
        Double Resultado = ResultadoClick;
        Toast Mensagem;
        if (ObjJogo.ResolverExercicio(CodExec,Resultado) == true) {
            Mensagem = Toast.makeText(getApplicationContext(),"Acertou!",Toast.LENGTH_SHORT);
            txtPontos.setText("Pontos: "+ObjJogo.getPontos());
            setInvisibleAlternativas();
        } else {
            Mensagem = Toast.makeText(getApplicationContext(),"Errou!",Toast.LENGTH_SHORT);
            txtPontos.setText("Pontos: "+ObjJogo.getPontos());
            setVisibleAlternativas();
        }
        Mensagem.show();
    }

    private void setVisibleAlternativas() {
        txtConta.setVisibility(View.VISIBLE);
        btnOpcao1.setVisibility(View.VISIBLE);
        btnOpcao2.setVisibility(View.VISIBLE);
        btnOpcao3.setVisibility(View.VISIBLE);
    }

    private void setInvisibleAlternativas() {
        txtConta.setVisibility(View.INVISIBLE);
        btnOpcao1.setVisibility(View.INVISIBLE);
        btnOpcao2.setVisibility(View.INVISIBLE);
        btnOpcao3.setVisibility(View.INVISIBLE);
    }

    private void PrepararComponentesGrid() {
        gvDadosJogo = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);
                text.setTextColor(getResources().getColor(R.color.black_overlay));

                text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                int Percentual = ObjJogo.getPercentual(position);
                int TempoRestante = ObjJogo.getPercentual(position);
                int Peso = ObjJogo.getPeso(position);
                boolean Ativo = ObjJogo.isAtivo(position);

                if ((Ativo == false) || (Percentual > 50)) {
                    view.setBackgroundColor(getResources().getColor(R.color.darkgreen));
                } else
                if ((Ativo == true) && (Percentual > 25)) {
                    view.setBackgroundColor(getResources().getColor(R.color.orange));
                }
                else
                if ((Ativo == true) && (Percentual > 0)) {
                    view.setBackgroundColor(getResources().getColor(R.color.red));
                }
                else
                if ((Ativo == true) && (Percentual == 0)) {
                    text.setTextColor(getResources().getColor(R.color.Transparente));
                    view.setBackgroundColor(getResources().getColor(R.color.Transparente));
                    view.setVisibility(View.INVISIBLE);
                }

                text.setText(TempoRestante+"s");
                return view;
            }
        };

        gvJogo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setVisibleAlternativas();
                if (ObjJogo.getPeso(CodExec) ==0) {
                    setInvisibleAlternativas();
                }
                CodExec = position;
                txtConta.setText(ObjJogo.getExercicio(position)+"=");
                Double[] Alternativas = ObjJogo.RetornaAlternativas(position);
                btnOpcao1.setText(String.format("%.0f", Alternativas[0]));
                btnOpcao2.setText(String.format("%.0f", Alternativas[1]));
                btnOpcao3.setText(String.format("%.0f", Alternativas[2]));
            }
        });
        gvJogo.setNumColumns(5);
        gvJogo.setAdapter(gvDadosJogo);

        ObjJogo = new Objetos.Jogo();
        ObjJogo.Preparar(ExerciciosEmTela,NivelDeDificuldade);
    }
}
