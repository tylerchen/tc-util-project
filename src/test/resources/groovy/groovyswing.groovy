import groovy.swing.SwingBuilder
import javax.swing.*
import java.awt.*

class Gwitter{   
  def searchField
  
  static void main(String[] args){
    def gwitter = new Gwitter()
    gwitter.show()
  }
    
  void show(){
    def swingBuilder = new SwingBuilder()  
    
    def customMenuBar = {
      swingBuilder.menuBar{
      	(1..3).each{
        menu(text: "File", mnemonic: 'F') {
          menuItem(text: "Exit", mnemonic: 'X', actionPerformed: {dispose() })
        }
        }
      }  
    }

    def searchPanel = {
      swingBuilder.scrollPane(){
      	ep = editorPane(contentType: "text/html", editable:false, page: new java.net.URL('http://localhost'))
      	ep.setSize(500, 800)
      	ep.getEditorKit().setAutoFormSubmission(false)
      	ep.addHyperlinkListener([
      		hyperlinkUpdate:{e->
      			println e.eventType
      			if( e.eventType == javax.swing.event.HyperlinkEvent.EventType.ACTIVATED ){
	      			if (e instanceof javax.swing.text.html.HTMLFrameHyperlinkEvent) {
	      				if(e instanceof javax.swing.text.html.FormSubmitEvent){
	      					println e.data
	      				}
	                     e.source.document.processHTMLFrameHyperlinkEvent(e);
	                 } else {
	                 	println 'aaaa'
	                     //e.source.page = e.getURL()
	                     e.source.text = '''
	                     <html>
	                     	<body style="background-color:white;">
	                     		<a href="#"><span class="aaa">hello</span></a>
	                     		<form method="get" action="test">
		                     		<select name="test">
		                     			<option>a</option>
		                     			<option>b</option>
		                     		</select>
		                     		<input type="submit" value="submit"/>
	                     		</form>
	                     	</body>
						</html>
	                     '''
	                 }
	                 //println e.getURL()
      			}
      		}
      	] as javax.swing.event.HyperlinkListener)
      }
    }
    
    swingBuilder.frame(title:"Gwitter", 
                       defaultCloseOperation:JFrame.EXIT_ON_CLOSE, 
                       size:[400,500],
                       show:true) {
      customMenuBar()                         
      searchPanel()
    }    
  }  
}