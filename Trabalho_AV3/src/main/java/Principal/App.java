package Principal;

import javax.swing.JOptionPane;

public class App extends javax.swing.JFrame {
	
	public App() {
		initComponents();
	}
	
	private void initComponents() {
		
		jPanel1 = new javax.swing.JPanel();
        btnDra = new javax.swing.JButton();
        btnDom = new javax.swing.JButton();
        lblEsc = new javax.swing.JLabel();
        btnMoby = new javax.swing.JButton();
        btnSair = new javax.swing.JButton();
        progressBar = new javax.swing.JProgressBar();
		
		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Análise Comparativa");
        
        lblEsc.setText("Escolha um livro abaixo:");
        
        btnDra.setText("Drácula");
        btnDra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDraActionPerformed(evt);
            }
        });
        
        btnDom.setText("DomQuixote");
        btnDom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDomActionPerformed(evt);
            }
        });
        
        btnMoby.setText("Moby Dick");
        btnMoby.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMobyActionPerformed(evt);
            }
        });
        
        btnSair.setText("Sair");
        btnSair.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSairActionPerformed(evt);
            }
        });
        
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(201, 201, 201)
                        .addComponent(lblEsc))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(90, 90, 90)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(progressBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(btnDom, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnMoby, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnDra, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(104, 104, 104))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnSair)
                .addGap(16, 16, 16))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addComponent(lblEsc)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnDom, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnMoby, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDra, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 64, Short.MAX_VALUE)
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(63, 63, 63)
                .addComponent(btnSair)
                .addGap(10, 10, 10))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
        
	}
	
	private void btnDomActionPerformed(java.awt.event.ActionEvent evt) {
		Controlador co = new Controlador();
		JOptionPane.showMessageDialog(null, "Iniciando processamento. Aguarde...", "Informação", JOptionPane.INFORMATION_MESSAGE);
		atualizarProgresso();
		co.controle(1);
		encerrar();
	}
	
	private void btnMobyActionPerformed(java.awt.event.ActionEvent evt) {
		Controlador co = new Controlador();
		JOptionPane.showMessageDialog(null, "Iniciando processamento. Aguarde...");
		atualizarProgresso();
		co.controle(2);
		encerrar();
	}
	
	private void btnDraActionPerformed(java.awt.event.ActionEvent evt) {
		Controlador co = new Controlador();
		JOptionPane.showMessageDialog(null, "Iniciando processamento. Aguarde...");
		atualizarProgresso();
		co.controle(3);
		encerrar();
	}
	
	private void btnSairActionPerformed(java.awt.event.ActionEvent evt) {
		encerrar();
	}
	
	public void atualizarProgresso() {
		progressBar.setStringPainted(true);
	    progressBar.setValue(100);
    }
	
	public void encerrar() {
		JOptionPane.showMessageDialog(null, "O programa será encerrado.", "Aviso", JOptionPane.WARNING_MESSAGE);
		System.exit(0);
	}
	
 	public static void main(String[] args) {
		
 		java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new App().setVisible(true);
            }
        });
	}
 	
 	private javax.swing.JButton btnDom;
    private javax.swing.JButton btnDra;
    private javax.swing.JButton btnMoby;
    private javax.swing.JButton btnSair;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblEsc;
    private javax.swing.JProgressBar progressBar;
}
