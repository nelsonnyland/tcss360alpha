package application.ui;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import application.model.Note;
import application.model.Project;
import application.ui.dialog.ProjectDialog;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;


/**
 * ProjectPanel is the selection pane for projects.
 *
 * @author Nelson Nyland
 */
public class ProjectPanel extends JPanel implements ListSelectionListener {

    private JScrollPane scrollPane;
    private NotePanel notePanel;
    private JButton newProjectButton;
    private static JList<Project> projects;
    private DefaultListModel<Project> projectModel;

    public ProjectPanel(NotePanel notePanel, DefaultListModel<Project> projectModel) {
        this.notePanel = notePanel;
        this.projectModel = projectModel;
        this.projects = new JList<>(projectModel);
        buildLayout();
        buildComponents();
        addComponents();
        addListeners();
    }

    private void buildLayout() {
        //setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setLayout(new BorderLayout());
        setAlignmentX(Component.CENTER_ALIGNMENT);
        setPreferredSize(new Dimension(150, ProjectPanel.HEIGHT));
    }

    private void buildComponents() {
        scrollPane = new JScrollPane(projects);
        scrollPane.setColumnHeaderView(new JLabel("Projects"));
        newProjectButton = new JButton("New Project");
    }

    private void addComponents() {
        add(scrollPane, BorderLayout.CENTER);
        add(newProjectButton, BorderLayout.SOUTH);
    }

    private void addListeners() {
        projects.addListSelectionListener(this);
        newProjectButton.addActionListener(this::newProject);
    }

    private void loadProject(Project selected) {
        //TODO: get notes affiliated with selected project by note ids
        // set notes panel
        notePanel.setNotePanel(buildNotes(selected));
        StringBuilder tags = new StringBuilder();
        if (selected.getTags() != null) {
            for (String tag : selected.getTags()) {
                tags.append(tag);
            }
        }
        ViewPanel.setText("Name: " + selected.getName() + "\n" + "Tags: " + tags);
    }

    private List<Note> buildNotes(Project selected) {
        List<Note> notes = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Note note = new Note();
            note.setId(i);
            note.setName("Project " + selected.getId() + " Note " + i);
            note.setContent("Here is content for Project " + selected.getId() + " Note " + i);
            notes.add(note);
        }
        return notes;
    }

    private void newProject(ActionEvent actionEvent) {
        new ProjectDialog(this);
    }

    public void addProject(Project project) {
        projectModel.addElement(project);
    }

    public void setProjectPanel(List<Project> projectList) {
        projectModel.clear();
        projectModel.addAll(projectList);
    }

    public int getProjectCount() {
        return projectModel.getSize();
    }

    public static int getProjectId() {
        int projectId = 0; //TODO: revise after db integration
        if (projects.getSelectedValue() != null) {
            projectId = projects.getSelectedValue().getId();
        }
        return projectId;
    }

    public static void addNoteId(int noteId) {
        if (projects.getSelectedValue() != null) {
            projects.getSelectedValue().addNoteId(noteId);
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            projects = (JList<Project>) e.getSource();
            Project selected = projects.getSelectedValue();
            loadProject(selected);
        }
    }
}