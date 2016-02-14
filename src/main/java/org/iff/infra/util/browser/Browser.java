/*******************************************************************************
 * Copyright (c) Feb 3, 2016 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util.browser;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.net.URL;

import javax.swing.Box;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.Scrollable;
import javax.swing.SwingUtilities;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkEvent.EventType;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.FormSubmitEvent;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.HTMLFrameHyperlinkEvent;

import org.apache.commons.lang3.StringUtils;
import org.iff.infra.util.Exceptions;
import org.iff.infra.util.FCS;
import org.iff.infra.util.Logger;

/**
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since Feb 3, 2016
 */
public class Browser implements HyperlinkListener, Runnable {

	private JFrame frame = new JFrame();
	private WorkBench workBench = new WorkBench();
	private JScrollPane scrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
			JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	private JEditorPane editorPane = new JEditorPane();
	private ActionListener actionListener;

	public static interface ActionListener {
		Object doAction(String url, String parameterString);
	}

	public Browser() {
		super();
		this.actionListener = new DefaultActionListener();
	}

	public Browser(ActionListener actionListener) {
		super();
		this.actionListener = actionListener == null ? new DefaultActionListener() : actionListener;
	}

	public void start() {
		SwingUtilities.invokeLater(this);
	}

	public ActionListener getActionListener() {
		return actionListener;
	}

	public void setActionListener(ActionListener actionListener) {
		this.actionListener = actionListener;
	}

	public void hyperlinkUpdate(HyperlinkEvent event) {
		if (event.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
			String url = null;
			try {
				url = event.getURL().toString();
			} catch (Exception e) {
				Logger.debug(FCS.get("Get URL fail. event: {0}", event), e);
			}
			String parameterString = "";
			if (event instanceof FormSubmitEvent) {
				parameterString = ((FormSubmitEvent) event).getData();
			}
			Logger.info(FCS.get("URL: {0}\nparameterString:{1}", url, parameterString));
			Object htmlOrUrl = actionListener.doAction(url, parameterString);
			if (htmlOrUrl != null && htmlOrUrl instanceof URL) {
				try {
					editorPane.setPage((URL) htmlOrUrl);
				} catch (Exception e) {
					Logger.debug(FCS.get("Goto URL fail. URL: {0}", url), e);
				}
			} else if (htmlOrUrl != null && htmlOrUrl instanceof CharSequence) {
				String html = htmlOrUrl.toString();
				if (StringUtils.isBlank(html)) {
					try {
						editorPane.setPage(event.getURL());
					} catch (Exception e) {
						Logger.debug(FCS.get("Goto URL fail. URL: {0}", url), e);
					}
				} else {
					((JEditorPane) event.getSource()).setText(StringUtils.defaultString(html, ""));
				}
			} else {
				try {
					editorPane.setPage(event.getURL());
				} catch (Exception e) {
					Logger.debug(FCS.get("Goto URL fail. URL: {0}", url), e);
				}
			}
		}
	}

	public void run() {
		try {
			{
				frame.setTitle("Browser");
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setSize(600, 400);
			}
			{
				scrollPane.setAutoscrolls(false);
				scrollPane.setViewportView(workBench);
			}
			frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
			{
				editorPane.setEditorKit(new HTMLEditorKit());
				editorPane.setOpaque(true);
				editorPane.setEditable(false);
				editorPane.setContentType("text/html");
				((HTMLEditorKit) editorPane.getEditorKit()).setAutoFormSubmission(false);
				editorPane.addHyperlinkListener(this);
				editorPane.getDocument().putProperty("stream", new URL("http://localhost"));
				{
					hyperlinkUpdate(new HTMLFrameHyperlinkEvent(editorPane, EventType.ACTIVATED,
							new URL("http://www.tc.com"), null));
				}
				editorPane.setBackground(Color.WHITE);
				{
					JPanel p = new JPanel(new BorderLayout());
					p.setBackground(Color.WHITE);
					p.add(editorPane, BorderLayout.CENTER);
					workBench.addPortal(p);
				}
			}
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					scrollPane.getViewport().setViewPosition(new java.awt.Point(0, 0));
				}
			});
			{
				// center the jframe, then make it visible
				frame.setSize(800, 600);
				frame.setLocationRelativeTo(null);
				frame.setVisible(true);
			}
		} catch (Exception e) {
			Exceptions.runtime("Can't start Browser.", e);
		}
	}

	public class WorkBench extends JPanel implements Scrollable {
		Box vbox = null;

		public WorkBench() {
			setLayout(new BorderLayout());
			this.vbox = Box.createVerticalBox();
			//**  Scenario A - no vertical scrolling  **
			add(this.vbox, BorderLayout.CENTER);
			//** Scenario B - no line wrapping **
			//JScrollPane sp = new JScrollPane(current );
			//sp.setViewportView(current );
			//add(sp, BorderLayout.CENTER);
		}

		public void addPortal(JPanel portal) {
			this.vbox.add(portal);
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

	public class DefaultActionListener implements ActionListener {
		public Object doAction(String url, String parameterString) {
			//			if ("http://localhost".equalsIgnoreCase(url)) {
			//				try {
			//					return new URL("http://layer.layui.com");
			//				} catch (Exception e) {
			//				}
			//			}
			return FCS
					.get("<html><body><a href='/'>Home</a><br>URL: {0}</br><br>ParameterString: {1}</br></body></html>",
							url, parameterString)
					.toString();
		}
	}
}
