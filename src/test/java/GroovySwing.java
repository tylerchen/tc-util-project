
/*******************************************************************************
 * Copyright (c) Feb 2, 2016 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Box;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JViewport;
import javax.swing.Scrollable;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.plaf.basic.BasicBorders;
import javax.swing.text.html.FormSubmitEvent;
import javax.swing.text.html.FormView;
import javax.swing.text.html.HTMLEditorKit;

import org.iff.infra.util.SocketHelper;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;

/**
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since Feb 2, 2016
 */
public class GroovySwing {

	public static void main(String[] args) {
		final Map<String, Object> map = new HashMap<String, Object>();
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFrame frame = new JFrame();
				{
					frame.setTitle("test");
					frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					frame.setSize(600, 400);
				}
				WorkBench right = new WorkBench();
				final JScrollPane sp = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
						JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
				sp.setAutoscrolls(false);
				sp.setViewportView(right);
				frame.getContentPane().add(sp, BorderLayout.CENTER);
				{
					final JEditorPane swingbox = new JEditorPane();
					swingbox.setEditorKit(new HTMLEditorKit());
					swingbox.setOpaque(true);
					swingbox.setEditable(false);
					swingbox.setContentType("text/html");
					{
						((HTMLEditorKit) swingbox.getEditorKit()).setAutoFormSubmission(false);
						swingbox.addHyperlinkListener(new HyperlinkListener() {
							public void hyperlinkUpdate(HyperlinkEvent event) {
								if (event.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
									swingbox.setText(
											"<html><body style='background-color:gray;'><form><input name='aa' type='text'/><input type='submit' value='submit'/></form><a href='#'>testaa</a></body></html>");
									if (event instanceof FormSubmitEvent) {
										System.out.println(((FormSubmitEvent) event).getData());
									}
								}
							}
						});
					}
					try {
						swingbox.setPage(new URL("http://182.254.204.165:8080/login.html"));
					} catch (Exception e) {
						e.printStackTrace();
					}
					swingbox.setBackground(Color.WHITE);
					JPanel p = new JPanel(new BorderLayout());
					p.setBackground(Color.WHITE);
					p.add(swingbox, BorderLayout.CENTER);
					right.addPortal(p);
				}
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						sp.getViewport().setViewPosition(new java.awt.Point(0, 0));
					}
				});
				// center the jframe, then make it visible
				frame.setSize(800, 600);
				frame.setLocationRelativeTo(null);
				frame.setVisible(true);
			}
		});
	}

	public static class WorkBench extends JPanel implements Scrollable {
		Box vertical_box = null;

		public WorkBench() {
			setLayout(new BorderLayout());
			this.vertical_box = Box.createVerticalBox();
			//**  Scenario A - no vertical scrolling  **
			add(this.vertical_box, BorderLayout.CENTER);
			//** Scenario B - no line wrapping **
			//JScrollPane sp = new JScrollPane(current );
			//sp.setViewportView(current );
			//add(sp, BorderLayout.CENTER);
		}

		public void addPortal(JPanel portal) {
			this.vertical_box.add(portal);
			validate();
		}

		public Dimension getPreferredScrollableViewportSize() {
			return getPreferredSize();
		}

		public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
			return 20;
		}

		public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
			return 60;
		}

		public boolean getScrollableTracksViewportWidth() {
			return true;
		}

		public boolean getScrollableTracksViewportHeight() {
			if (getParent() instanceof JViewport) {
				return (((JViewport) getParent()).getHeight() > getPreferredSize().height);
			}
			return false;
		}
	}

	public static void main2(String[] args) throws Exception {
		final JFrame frame = new JFrame();
		{
			frame.setTitle("test");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setSize(600, 400);
		}
		final JScrollPane scrollPanel = new JScrollPane();
		final JTextPane editorPanel = new JTextPane();
		{
			//scrollPane.setAutoscrolls(false);
			frame.setLayout(new BorderLayout());
			frame.add(scrollPanel, BorderLayout.CENTER);
			scrollPanel.setBorder(BasicBorders.getInternalFrameBorder());
			frame.addComponentListener(new ComponentListener() {
				public void componentShown(ComponentEvent paramComponentEvent) {
					//scrollPane.setSize(frame.getContentPane().getWidth(), frame.getContentPane().getHeight());
					//editorPane.setSize(frame.getContentPane().getWidth(), frame.getContentPane().getHeight());
					//editorPane.repaint();
				}

				public void componentResized(ComponentEvent event) {
					//scrollPane.setSize(frame.getWidth(), frame.getHeight());
					//editorPane.setSize(frame.getSize());
					//editorPane.setSize(frame.getContentPane().getWidth(), frame.getContentPane().getHeight());
				}

				public void componentMoved(ComponentEvent paramComponentEvent) {
					//scrollPane.setSize(frame.getContentPane().getWidth(), frame.getContentPane().getHeight());
					//editorPane.setSize(frame.getContentPane().getWidth(), frame.getContentPane().getHeight());
					//editorPane.repaint();
				}

				public void componentHidden(ComponentEvent paramComponentEvent) {
					//scrollPane.setSize(frame.getContentPane().getWidth(), frame.getContentPane().getHeight());
					//editorPane.setSize(frame.getContentPane().getWidth(), frame.getContentPane().getHeight());
					//editorPane.repaint();
				}
			});
		}
		{
			editorPanel.setSize(new Dimension(400, 300));
			editorPanel.setMinimumSize(new Dimension(400, 300));
			editorPanel.setEditable(false);
			editorPanel.setContentType("text/html");
			((HTMLEditorKit) editorPanel.getEditorKit()).setAutoFormSubmission(false);
			editorPanel.addHyperlinkListener(new HyperlinkListener() {
				public void hyperlinkUpdate(HyperlinkEvent event) {
					if (event.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
						editorPanel.setText(
								"<html><body style='background-color:gray;'><form><input name='aa' type='text'/><input type='submit' value='submit'/></form><a href='#'>testaa</a></body></html>");
						if (event instanceof FormSubmitEvent) {
							System.out.println(((FormSubmitEvent) event).getData());
						}
					}
				}
			});
			//editorPane.getDocument().putProperty("stream", "http://localhost");
			editorPanel.setPage(new URL("http://182.254.204.165:8080/login.html"));
			//editorPane.getDocument().putProperty("stream", new URL("http://localhost"));
			//			editorPane.setText(
			//					"<html><body style='background-color:gray;height:100px;'><form><input name='aa' type='text'/><input type='submit' value='submit'/></form><a href='#'>test</a><div style='height:1000px;'>aaaa</div></body></html>");
		}
		{
			scrollPanel.add(editorPanel);
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
				}
			});
		}
		{
			frame.setVisible(true);
		}
	}

	public static void main1(String[] args) {
		GroovyClassLoader gcl = new GroovyClassLoader();
		try {
			Class clazz = gcl.parseClass(SocketHelper.getContent(
					new FileInputStream(
							"/Users/zhaochen/dev/workspace/cocoa/tc-util-project/src/test/resources/groovy/groovyswing.groovy"),
					false));
			GroovyObject obj = (GroovyObject) clazz.newInstance();
			obj.invokeMethod("main", null);//javax.swing.text.html.HTML.Tag.
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
/*
[Menu][File]:[Key][Shade][Action]
[Menu][File][Exit]:[Key][Shade][Action]
[Toolbar][Open]:[Key][Shade][Action]
[Toolbar][Open][Sub]:[Key][Shade][Action]
[Layout]:[4][4]
[Layout][0][1]:[Shade][Action]
[Footbar]:
 */
