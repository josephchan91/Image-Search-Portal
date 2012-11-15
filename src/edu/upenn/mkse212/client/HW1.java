package edu.upenn.mkse212.client;

import java.util.ArrayList;

import java.util.List;
import java.util.Set;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.NativeVerticalScrollbar;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class HW1 implements EntryPoint {
  /**
   * The message displayed to the user when the server cannot be reached or
   * returns an error.
   */
  private static final String SERVER_ERROR = "An error occurred while "
		  + "attempting to contact the server. Please check your network "
		  + "connection and try again.";

  /**
   * Create a remote service proxy to talk to the server-side image-search service.
   */
  private final ImageServiceAsync imageSearchService = GWT.create(ImageService.class);

  private List<String> imageMatches;

  // CHANGE THESE AROUND
  public final static int WIDTH = 2;
  public final static int HEIGHT = 1;
  public final static int IMAGE_WIDTH = 120;
  public final static int IMAGE_HEIGHT = 120;
  // ^^^CHANGE THESE AROUND
  
  // Adjusted widths for when imageMatches < WIDTH * HEIGHT
  private int newWidth = WIDTH;
  private int newHeight = HEIGHT;
  final int SCROLL_PANEL_PADDING = 20;  // buffer for scrollbar width
  final Grid searchResults = new Grid(1,1);
  final HTML resultsLabel = new HTML("<b>Matching images:</b>");
  // Counters for pagination mode
  private int currentIndex = 0;
  private int currentPageCount = 0;
  // Stuff for scrolling capabilities
  final private ScrollPanel scrollable = new ScrollPanel();
  final private CheckBox enableScrolling = new CheckBox("Enable scrolling");

  public void redraw() 
  {  
	// CHECK FOR EMPTY SET
	int numberOfMatches = imageMatches.size();
	if (numberOfMatches == 0) {
		searchResults.resize(4, 3);
		resultsLabel.setHTML("<b>Sorry, no matches found.</b>");
		scrollable.setSize(Integer.toString(resultsLabel.getOffsetWidth()), Integer.toString(resultsLabel.getOffsetHeight()));
	    searchResults.clear(true);
	    searchResults.setHTML(0, 0, "");
	    return;
	}
	
	// If scrolling enabled
	if (enableScrolling.getValue()) {
		newWidth = numberOfMatches < WIDTH ? numberOfMatches : WIDTH;
		double newHeightDouble = Math.ceil((double)numberOfMatches / (double)newWidth);
		newHeight = (int) newHeightDouble;
		// The height of the scrollable area
		int scrollableGridHeight = newHeight < HEIGHT ? newHeight : HEIGHT;
		searchResults.resize(newHeight, newWidth);
		resultsLabel.setHTML("<b>" + numberOfMatches + " match/es found:</b>");
		// Adjust scrollable area for when imageMatches are less than min capacity
		scrollable.setSize(Integer.toString(IMAGE_WIDTH * newWidth + SCROLL_PANEL_PADDING) + "px",
				Integer.toString(scrollableGridHeight * IMAGE_HEIGHT + SCROLL_PANEL_PADDING) + "px");
	    searchResults.clear(true);
	    int counter = 0;
		for (int row = 0; row < newHeight; row++) { 
		      for (int col = 0; col < newWidth; col++) {
		    	  if (counter == numberOfMatches) break;
		    	  String imageURL = imageMatches.get(counter);
		    	  searchResults.setHTML(row, col, "<a href=\"" + imageURL
		    			  + "\" target=_blank><img src=\"" + imageURL
		    			  + "\" width="+IMAGE_WIDTH+" height="+IMAGE_HEIGHT+"></a>");
		    	  counter++;
		      }
		}
	} else { // Otherwise, paginate
		newWidth = WIDTH;
		newHeight = HEIGHT;
		int matchesRemaining = numberOfMatches - currentIndex;
		int indexUpTo = currentIndex + WIDTH * HEIGHT;
		// Check if imageMatches less than min capacity
		if (matchesRemaining < WIDTH * HEIGHT) {
			indexUpTo = currentIndex + matchesRemaining;
			newHeight = (matchesRemaining / WIDTH) + 1;
			newWidth = matchesRemaining < WIDTH ? matchesRemaining : WIDTH;
		}
		// The height of the scrollable area
		int scrollableGridHeight = newHeight < HEIGHT ? newHeight : HEIGHT;
		searchResults.resize(newHeight, newWidth);
	    resultsLabel.setHTML("<b>Images " + (currentIndex + 1) + 
	    		" to " + indexUpTo + " of " + numberOfMatches + ":</b>");
	    // Adjust scrollable area for when imageMatches are less than min capacity
	    scrollable.setSize(Integer.toString(IMAGE_WIDTH * newWidth + SCROLL_PANEL_PADDING) + "px",
	    		Integer.toString(scrollableGridHeight * IMAGE_HEIGHT + SCROLL_PANEL_PADDING) + "px");
	    searchResults.clear(true);
	    currentPageCount = 0;
	    for (int row = 0; row < Math.max(HEIGHT,newHeight); row++) { 
	      for (int col = 0; col < Math.max(WIDTH,newWidth); col++) {
	    	  if (currentIndex == numberOfMatches) break;
	    	  String imageURL = imageMatches.get(currentIndex);
	    	  searchResults.setHTML(row, col, "<a href=\"" + imageURL
	    			  + "\" target=_blank><img src=\"" + imageURL
	    			  + "\" width="+IMAGE_WIDTH+" height="+IMAGE_HEIGHT+"></a>"); 
	    	  currentIndex++;
	    	  currentPageCount++;
	      }
	    }
	}
  }

  /**
   * This is the entry point method.
   */
  public void onModuleLoad() {
    final Button sendButton = new Button("Send");
    final TextBox nameField = new TextBox();
    nameField.setText("Avocado");
    final Label errorLabel = new Label();

    // Initialize the imageMatches to a string ArrayList, by default.
    imageMatches = new ArrayList<String>();

    // We can add style names to widgets
    sendButton.addStyleName("sendButton");

    // Add the nameField and sendButton to the RootPanel
    // Use RootPanel.get() to get the entire body element
    RootPanel.get("nameFieldContainer").add(nameField);
    RootPanel.get("sendButtonContainer").add(sendButton);
    RootPanel.get("errorLabelContainer").add(errorLabel);
    // Add a wrapper around grid to enable scrolling
    RootPanel.get("scrollbarCheckboxContainer").add(enableScrolling);

    // Focus the cursor on the name field when the app loads
    nameField.setFocus(true);
    nameField.selectAll();

    // Create the popup dialog box
    final DialogBox dialogBox = new DialogBox();
    dialogBox.setText("Search Results Pop-up");
    // I disabled this to avoid the gwt problem of dialog box size
    //dialogBox.setAnimationEnabled(true);
    
    // Add buttons
    final Button prevButton = new Button("Previous");
    // We can set the id of a widget by accessing its Element
    prevButton.getElement().setId("prevButton");
    prevButton.setEnabled(false);
    final Button nextButton = new Button("Next");
    // We can set the id of a widget by accessing its Element
    nextButton.getElement().setId("nextButton");
    nextButton.setEnabled(false);
    final Button closeButton = new Button("Close");
    // We can set the id of a widget by accessing its Element
    closeButton.getElement().setId("closeButton");

    HorizontalPanel buttonPanel = new HorizontalPanel();
    buttonPanel.addStyleName("dialogHPanel");
    buttonPanel.add(prevButton);
    buttonPanel.add(nextButton);
    buttonPanel.add(closeButton);
    
    VerticalPanel dialogVPanel = new VerticalPanel();
    dialogVPanel.addStyleName("dialogVPanel");
    dialogVPanel.add(resultsLabel);
    searchResults.setCellPadding(0);
    searchResults.setCellSpacing(0);
    // Wrap the grid with a scroll panel and add the scrollpanel to the dialogbox
    scrollable.add(searchResults);
    dialogVPanel.add(scrollable);
    dialogVPanel.add(buttonPanel);
    dialogBox.setWidget(dialogVPanel);

    // Add a handler to close the DialogBox
    closeButton.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
	dialogBox.hide();
	sendButton.setEnabled(true);
	sendButton.setFocus(true);
	enableScrolling.setEnabled(true);
      }
    });

    // Add a handler to move to previous
    prevButton.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
    	  // Enable/disable prev button accordingly
    	  currentIndex -= WIDTH * HEIGHT + currentPageCount;
    	  if (currentIndex >= WIDTH * HEIGHT) {
    		  prevButton.setEnabled(true);
    	  } else {
    		  prevButton.setEnabled(false);
    	  }
    	  nextButton.setEnabled(true); 
    	  redraw();
      }
    });

    // Add a handler to move next
    nextButton.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
    	// Enable/disable next button accordingly
    	  if (imageMatches.size() - currentIndex > WIDTH * HEIGHT) {
    		  nextButton.setEnabled(true);
    	  } else {
    		  nextButton.setEnabled(false);
    	  }
    	  prevButton.setEnabled(true);
    	  redraw();
      }
    });

    // Create a handler for the sendButton and nameField
    class MyHandler implements ClickHandler, KeyUpHandler {
      /**
       * Fired when the user clicks on the sendButton.
       */
      public void onClick(ClickEvent event) {
	sendTermToServer();
      }

      /**
       * Fired when the user types in the nameField.
       */
      public void onKeyUp(KeyUpEvent event) {
	if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) 
	  sendTermToServer();
      }

      /**
       * Send the name from the nameField to the server and wait for a response.
       */
      private void sendTermToServer() {
	// First, we validate the input.
	errorLabel.setText("");
	String textToServer = nameField.getText();

	if (textToServer.contains(" ")) {
	  errorLabel.setText("Please use a single keyword only");
	  return;
	}

	// Then, we send the input to the server.
	sendButton.setEnabled(false);
	enableScrolling.setEnabled(false);
	imageSearchService.fetchImageResults(textToServer,
	  new AsyncCallback<Set<String>>() {
	    public void onFailure(Throwable caught) {
	      // Show the RPC error message to the user
	      dialogBox.setText("Remote Procedure Call - Failure");
	      searchResults.addStyleName("serverResponseLabelError");
	      searchResults.setHTML(0,0, SERVER_ERROR);
	      dialogBox.center();
	      closeButton.setFocus(true);
	    }

	    /**
	     * This function is called when the queryImages request is finished successfully.
	     * @param results
	     */
	    public void onSuccess(Set<String> results) {
	    	currentIndex = 0;
	      // First, set the nameField to the current keyword
	      dialogBox.setText("Search Results for " + nameField.getText());
	      searchResults.removeStyleName("serverResponseLabelError");

	      // Store the image set to the imageMatches list.
	      imageMatches = new ArrayList<String>();
	      imageMatches.addAll(results);
	      
  	      // TODO: Enable or disable previous/next buttons as appropriate 
	      if (enableScrolling.getValue()) {
	    	  nextButton.setEnabled(false);
	    	  prevButton.setEnabled(false);
	      } else {
		      if (imageMatches.size() > WIDTH * HEIGHT) {
		    	  nextButton.setEnabled(true);
		    	  prevButton.setEnabled(false);
		      } else {
		    	  nextButton.setEnabled(false);
		    	  prevButton.setEnabled(false);
		      }
	      }
	      
	      redraw();
	      dialogBox.center();
	      closeButton.setFocus(true);
	    }
	  });
      }
    }

    // Add a handler to send the name to the server
    MyHandler handler = new MyHandler();
    sendButton.addClickHandler(handler);
    nameField.addKeyUpHandler(handler);
  }
}
