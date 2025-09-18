import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

// ===== Data Models =====
class User {
    String username, password, role;
    public User(String u, String p, String r){ username=u; password=p; role=r; }
}

class Question {
    String text, A, B, C, D, correct;
    public Question(String t, String a, String b, String c, String d, String corr){
        text=t; A=a; B=b; C=c; D=d; correct=corr;
    }
}

class Result {
    String username; int score;
    public Result(String u, int s){ username=u; score=s; }
}

// ===== Main Application =====
public class OnlineExamSystem extends JFrame {
    private static java.util.List<User> users = new ArrayList<>();
    private static java.util.List<Question> questions = new ArrayList<>();
    private static java.util.List<Result> results = new ArrayList<>();
    private User currentUser;

    public OnlineExamSystem(){
        users.add(new User("admin","admin","ADMIN"));
        preloadQuestions();
        showLogin();
    }

    private void preloadQuestions(){
        questions.add(new Question("Capital of India?","Mumbai","Chennai","New Delhi","Kolkata","C"));
        questions.add(new Question("Who wrote 'Romeo and Juliet'?","Mark Twain","Shakespeare","Dickens","Austen","B"));
        questions.add(new Question("Chemical symbol for water?","O2","H2O","CO2","NaCl","B"));
        questions.add(new Question("Red Planet?","Earth","Venus","Mars","Jupiter","C"));
        questions.add(new Question("What is the capital of India?","Mumbai","New Delhi","Kolkata","Hyderabad","B"));
        questions.add(new Question(
        "Who is known as the Father of Java?",
        "James Gosling","Dennis Ritchie","Bjarne Stroustrup","Guido van Rossum","A"));
        questions.add(new Question(
        "Which data structure uses FIFO principle?",
        "Stack","Queue","Tree","Graph","B"));
        questions.add(new Question(
        "What does HTML stand for?",
        "Hyper Text Markup Language","High Text Machine Language","Hyper Tabular Markup Language","None of these","A"));
        questions.add(new Question(
        "Which is not an OOP concept in Java?",
        "Encapsulation","Polymorphism","Inheritance","Compilation","D"));
        questions.add(new Question("Which keyword is used to inherit a class in Java?",
        "this","super","extends","implement","C"));
        questions.add(new Question(
        "Which collection class allows duplicates?",
        "Set","Map","List","None","C"));
        questions.add(new Question(
        "Which of these is a checked exception in Java?",
        "IOException","NullPointerException","ArithmeticException","ArrayIndexOutOfBoundsException","A"));
        questions.add(new Question(
        "Which SQL command is used to fetch data?",
        "GET","SELECT","RETRIEVE","FETCH","B"));
        questions.add(new Question(
        "Which company developed Python?",
        "Microsoft","Sun Microsystems","Google","Python Software Foundation","D"));
        questions.add(new Question(
        "What is the size of int in Java?",
        "2 bytes","4 bytes","8 bytes","Depends on OS","B"));
        questions.add(new Question(
        "Which of these is not a programming language?",
        "C++","Python","MS Word","Java","C"));
        questions.add(new Question(
        "Which layer in OSI model is responsible for routing?",
        "Transport","Network","Session","Physical","B"));
        questions.add(new Question(
        "Which is the default port for HTTP?","20","21","80","443","C"));
        questions.add(new Question("Which one is not a NoSQL database?","MongoDB","Cassandra","MySQL","CouchDB","C"));
    }

    // ===== LOGIN / SIGNUP =====
    private void showLogin(){
        getContentPane().removeAll(); 
        setTitle("Login"); 
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5); 
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblUser=new JLabel("Username:"); 
        gbc.gridx=0; gbc.gridy=0; add(lblUser,gbc);
        JTextField tfUser=new JTextField(15); 
        gbc.gridx=1; add(tfUser,gbc);

        JLabel lblPass=new JLabel("Password:"); 
        gbc.gridx=0; gbc.gridy=1; add(lblPass,gbc);
        JPasswordField pfPass=new JPasswordField(15); 
        gbc.gridx=1; add(pfPass,gbc);

        // === Login + Signup Buttons in One Panel ===
        JButton btnLogin=new JButton("Login"); 
        JButton btnSignup=new JButton("Signup");
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnLogin);
        buttonPanel.add(btnSignup);
        gbc.gridx=0; gbc.gridy=2; gbc.gridwidth=2;
        add(buttonPanel,gbc);

        JLabel lblMessage=new JLabel(" "); 
        gbc.gridx=0; gbc.gridy=3; gbc.gridwidth=2; 
        add(lblMessage,gbc);

        // === Actions ===
        btnLogin.addActionListener(e->{
            String u=tfUser.getText().trim(); 
            String p=new String(pfPass.getPassword()).trim();
            Optional<User> userOpt = users.stream().filter(us->us.username.equals(u) && us.password.equals(p)).findFirst();
            if(userOpt.isPresent()){ 
                currentUser=userOpt.get(); 
                lblMessage.setText("Login Successful!");
                if(currentUser.role.equals("ADMIN")) showAdminPanel(); else showStudentPanel();
            } else lblMessage.setText("Invalid username or password!");
        });

        btnSignup.addActionListener(e->{
            String u=tfUser.getText().trim(); 
            String p=new String(pfPass.getPassword()).trim();
            if(u.isEmpty()||p.isEmpty()){ lblMessage.setText("Enter username and password!"); return; }
            if(users.stream().anyMatch(us->us.username.equals(u))){ lblMessage.setText("Username exists!"); return; }
            users.add(new User(u,p,"STUDENT")); 
            lblMessage.setText("Signup successful! Login now.");
        });

        setSize(400,250); 
        setLocationRelativeTo(null); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        setVisible(true);
        revalidate(); repaint();
    }

    // ===== ADMIN PANEL =====
    private void showAdminPanel(){
        getContentPane().removeAll(); 
        setTitle("Admin Panel"); 
        setLayout(new BorderLayout());
        JLabel lblTitle=new JLabel("Admin - Manage Questions & Results",SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial",Font.BOLD,16)); 
        add(lblTitle,BorderLayout.NORTH);

        DefaultListModel<String> listModel = new DefaultListModel<>();
        for(Question q:questions) listModel.addElement(q.text);
        JList<String> questionList = new JList<>(listModel);
        add(new JScrollPane(questionList),BorderLayout.CENTER);

        JPanel bottom = new JPanel();
        JButton btnAdd=new JButton("Add"); 
        JButton btnEdit=new JButton("Edit"); 
        JButton btnDelete=new JButton("Delete");
        JButton btnResults=new JButton("View Results"); 
        JButton btnLogout=new JButton("Logout");
        bottom.add(btnAdd); bottom.add(btnEdit); bottom.add(btnDelete); bottom.add(btnResults); bottom.add(btnLogout);
        add(bottom,BorderLayout.SOUTH);

        btnAdd.addActionListener(e->addOrEditQuestion(listModel,null));
        btnEdit.addActionListener(e->{
            int idx=questionList.getSelectedIndex();
            if(idx>=0) addOrEditQuestion(listModel,questions.get(idx));
        });
        btnDelete.addActionListener(e->{
            int idx=questionList.getSelectedIndex();
            if(idx>=0){ questions.remove(idx); listModel.remove(idx); }
        });
        btnResults.addActionListener(e->{
            StringBuilder sb = new StringBuilder();
            for(Result r:results) sb.append(r.username).append(" - Score: ").append(r.score).append("\n");
            JOptionPane.showMessageDialog(this, sb.length()>0?sb.toString():"No results yet.");
        });
        btnLogout.addActionListener(e->showLogin());

        setSize(700,500); setLocationRelativeTo(null); revalidate(); repaint();
    }

    private void addOrEditQuestion(DefaultListModel<String> model, Question q){
        JTextField tfQ = new JTextField(q!=null?q.text:"",20);
        JTextField tfA = new JTextField(q!=null?q.A:"",10);
        JTextField tfB = new JTextField(q!=null?q.B:"",10);
        JTextField tfC = new JTextField(q!=null?q.C:"",10);
        JTextField tfD = new JTextField(q!=null?q.D:"",10);
        JTextField tfCorrect = new JTextField(q!=null?q.correct:"",2);

        JPanel panel = new JPanel(new GridLayout(6,2));
        panel.add(new JLabel("Question:")); panel.add(tfQ);
        panel.add(new JLabel("Option A:")); panel.add(tfA);
        panel.add(new JLabel("Option B:")); panel.add(tfB);
        panel.add(new JLabel("Option C:")); panel.add(tfC);
        panel.add(new JLabel("Option D:")); panel.add(tfD);
        panel.add(new JLabel("Correct (A/B/C/D):")); panel.add(tfCorrect);

        int res = JOptionPane.showConfirmDialog(this,panel,q==null?"Add Question":"Edit Question",JOptionPane.OK_CANCEL_OPTION);
        if(res==JOptionPane.OK_OPTION){
            if(q==null){
                Question nq = new Question(tfQ.getText(),tfA.getText(),tfB.getText(),tfC.getText(),tfD.getText(),tfCorrect.getText().toUpperCase());
                questions.add(nq); model.addElement(nq.text);
            } else {
                q.text=tfQ.getText(); q.A=tfA.getText(); q.B=tfB.getText(); q.C=tfC.getText(); q.D=tfD.getText(); q.correct=tfCorrect.getText().toUpperCase();
                model.setElementAt(q.text, model.indexOf(q.text));
            }
        }
    }

    // ===== STUDENT PANEL =====
    private void showStudentPanel(){
        getContentPane().removeAll(); 
        setTitle("Student Panel"); 
        setLayout(new BorderLayout());
        JLabel lblTitle=new JLabel("Welcome "+currentUser.username,SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial",Font.BOLD,16)); 
        add(lblTitle,BorderLayout.NORTH);

        JPanel panel = new JPanel();
        JButton btnExam=new JButton("Start Exam");
        JButton btnHistory=new JButton("View Results");
        JButton btnChangePass=new JButton("Change Password");
        JButton btnLogout=new JButton("Logout");
        panel.add(btnExam); panel.add(btnHistory); panel.add(btnChangePass); panel.add(btnLogout);
        add(panel,BorderLayout.CENTER);

        btnExam.addActionListener(e->startExamOneByOne());
        btnHistory.addActionListener(e->{
            StringBuilder sb = new StringBuilder();
            for(Result r:results) if(r.username.equals(currentUser.username)) sb.append("Score: ").append(r.score).append("\n");
            JOptionPane.showMessageDialog(this,sb.length()>0?sb.toString():"No past results.");
        });
        btnChangePass.addActionListener(e->changePassword());
        btnLogout.addActionListener(e->{ currentUser=null; showLogin(); });

        setSize(500,300); setLocationRelativeTo(null); revalidate(); repaint();
    }

    // ===== EXAM (ONE QUESTION AT A TIME) =====
    private void startExamOneByOne(){
        if(questions.isEmpty()){ JOptionPane.showMessageDialog(this,"No questions available."); return; }

        getContentPane().removeAll(); 
        setLayout(new BorderLayout());
        JLabel lblTimer = new JLabel("Time left: 05:00",SwingConstants.CENTER); 
        lblTimer.setFont(new Font("Arial",Font.BOLD,16));
        add(lblTimer,BorderLayout.NORTH);

        JPanel questionPanel = new JPanel(new GridLayout(5,1));
        add(questionPanel,BorderLayout.CENTER);

        JButton btnNext = new JButton("Next"); 
        JButton btnPrev = new JButton("Previous"); 
        JButton btnSubmit = new JButton("Submit");
        JPanel navPanel = new JPanel(); 
        navPanel.add(btnPrev); navPanel.add(btnNext); navPanel.add(btnSubmit);
        add(navPanel,BorderLayout.SOUTH);

        int totalQuestions = questions.size();
        int[] currentIndex = {0};
        String[] answers = new String[totalQuestions];
        ButtonGroup bg = new ButtonGroup();
        JRadioButton[] optionButtons = new JRadioButton[4];

        ActionListener displayQuestion = e -> {
            questionPanel.removeAll();
            bg.clearSelection();
            Question q = questions.get(currentIndex[0]);
            questionPanel.setBorder(BorderFactory.createTitledBorder("Q"+(currentIndex[0]+1)));
            questionPanel.add(new JLabel(q.text));
            optionButtons[0]=new JRadioButton("A. "+q.A);
            optionButtons[1]=new JRadioButton("B. "+q.B);
            optionButtons[2]=new JRadioButton("C. "+q.C);
            optionButtons[3]=new JRadioButton("D. "+q.D);
            for(int i=0;i<4;i++){ 
                bg.add(optionButtons[i]); 
                questionPanel.add(optionButtons[i]);
                int idx=i; 
                optionButtons[i].addActionListener(ev->{ answers[currentIndex[0]]=""+(char)('A'+idx); });
            }
            if(answers[currentIndex[0]]!=null) optionButtons[answers[currentIndex[0]].charAt(0)-'A'].setSelected(true);
            questionPanel.revalidate(); questionPanel.repaint();
        };
        displayQuestion.actionPerformed(null);

        btnNext.addActionListener(e->{ if(currentIndex[0]<totalQuestions-1){ currentIndex[0]++; displayQuestion.actionPerformed(null); } });
        btnPrev.addActionListener(e->{ if(currentIndex[0]>0){ currentIndex[0]--; displayQuestion.actionPerformed(null); } });
        btnSubmit.addActionListener(e->{ submitExam(answers); });

        final int[] remainingSeconds = {300};
        javax.swing.Timer examTimer = new javax.swing.Timer(1000,a->{
            remainingSeconds[0]--;
            int min=remainingSeconds[0]/60; int sec=remainingSeconds[0]%60;
            lblTimer.setText(String.format("Time left: %02d:%02d",min,sec));
            if(remainingSeconds[0]<=0){ ((javax.swing.Timer)a.getSource()).stop(); submitExam(answers); }
        });
        examTimer.start();

        setSize(700,500); setLocationRelativeTo(null); revalidate(); repaint();
    }

    private void submitExam(String[] answers){
        int score=0;
        for(int i=0;i<questions.size();i++){
            if(answers[i]!=null && answers[i].equalsIgnoreCase(questions.get(i).correct)) score+=10;
        }
        results.add(new Result(currentUser.username,score));
        JOptionPane.showMessageDialog(this,"Exam submitted! Your score: "+score);
        showStudentPanel();
    }

    // ===== CHANGE PASSWORD =====
    private void changePassword(){
        JPanel panel=new JPanel(new GridLayout(3,2));
        JPasswordField pfCurr=new JPasswordField(10), pfNew=new JPasswordField(10), pfConf=new JPasswordField(10);
        panel.add(new JLabel("Current Password:")); panel.add(pfCurr);
        panel.add(new JLabel("New Password:")); panel.add(pfNew);
        panel.add(new JLabel("Confirm New:")); panel.add(pfConf);

        int option = JOptionPane.showConfirmDialog(this,panel,"Change Password",JOptionPane.OK_CANCEL_OPTION);
        if(option==JOptionPane.OK_OPTION){
            String curr=new String(pfCurr.getPassword()).trim();
            String nw=new String(pfNew.getPassword()).trim();
            String conf=new String(pfConf.getPassword()).trim();
            if(!curr.equals(currentUser.password)){ JOptionPane.showMessageDialog(this,"Incorrect current password"); return; }
            if(!nw.equals(conf)){ JOptionPane.showMessageDialog(this,"New passwords do not match"); return; }
            currentUser.password=nw; JOptionPane.showMessageDialog(this,"Password changed successfully!");
        }
    }

    public static void main(String[] args){ SwingUtilities.invokeLater(OnlineExamSystem::new); }
}
