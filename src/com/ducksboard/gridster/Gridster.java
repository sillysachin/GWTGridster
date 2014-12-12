package com.ducksboard.gridster;

import com.google.gwt.core.client.IJavaScriptWrapper;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.ResizeComposite;

public class Gridster extends ResizeComposite implements IJavaScriptWrapper<GridsterJSO>
{
	protected GridsterJSO jso;

	private String id;

	Integer counter = 0;

	private LayoutPanel divWrapper = new LayoutPanel();

	private UnorderedListWidget ulWidget = new UnorderedListWidget();

	private FlowPanel divWidget = new FlowPanel();

	private DecoratedPopupPanel simplePopup = new DecoratedPopupPanel( true );

	private IsWidget menuWidget;

	public Gridster()
	{
		init();
	}

	private void init()
	{
		String id = Document.get().createUniqueId();
		setId( id );
		divWidget.addStyleName( "gridster" );
		divWidget.add( ulWidget );
		divWrapper.add( divWidget );
		simplePopup.ensureDebugId( "cwBasicPopup-simplePopup" );
		simplePopup.setWidth( "150px" );
		initWidget( divWrapper );
	}

	private void init( JavaScriptObject configJSO )
	{
		init();
		makeGridster( getId(), configJSO );
	}

	public void setId( String id )
	{
		this.id = id;
		divWrapper.getElement().setId( id );
	}

	public String getId()
	{
		return id;
	}

	@Override
	public GridsterJSO getJso()
	{
		return this.jso;
	}

	@Override
	public void setJso( GridsterJSO jso )
	{
		this.jso = jso;
	}

	@Override
	protected void onLoad()
	{
		final String id = getId();
		jso = makeGridster( id );
	}

	protected static native GridsterJSO makeGridster( String id )
	/*-{
		var gridster;

		$wnd.$(function() {
			gridster = $wnd.$(".gridster ul").gridster({
				widget_margins : [ 10, 10 ],
				widget_base_dimensions : [ 140, 140 ],
				min_cols : 6,
				helper : 'clone',
				resize : {
					enabled : true
				}
			}).data('gridster');
			this.@com.ducksboard.gridster.Gridster::jso = gridster;
		});
		return gridster;
	}-*/;

	protected static native GridsterJSO makeGridster( String id, JavaScriptObject configJSO )
	/*-{
		var gridster;
		$wnd.$(function() {
			gridster = $wnd.$(".gridster ul").gridster({
				widget_margins : [ 10, 10 ],
				widget_base_dimensions : [ 200, 200 ],
				min_cols : 6,
				helper : 'clone',
				resize : {
					enabled : true
				},
				counter : 0
			}).data('gridster');
			this.@com.ducksboard.gridster.Gridster::jso = gridster;
		});
		return gridster;
	}-*/;

	public void addWidget( String textContent )
	{
		counter++;
		final String idPrefix = id + "-" + counter;
		addWidget( idPrefix, textContent );
		Element deleteElem = DOM.getElementById( "img-delete-" + idPrefix );
		DOM.sinkEvents( deleteElem, Event.ONCLICK | Event.ONMOUSEOUT | Event.ONMOUSEOVER );
		DOM.setEventListener( deleteElem, new EventListener()
		{
			public void onBrowserEvent( Event event )
			{
				if ( Event.ONCLICK == event.getTypeInt() )
				{
					Element target = event.getTarget();
					String targetId = target.getId();
					if ( targetId.contains( "delete" ) )
					{
						removeWidgetById( "li-" + idPrefix );
					}
				}
			}
		} );
		final Element addElem = DOM.getElementById( "img-add-" + idPrefix );
		DOM.sinkEvents( addElem, Event.ONCLICK | Event.ONMOUSEOUT | Event.ONMOUSEOVER );
		DOM.setEventListener( addElem, new EventListener()
		{
			public void onBrowserEvent( Event event )
			{
				if ( Event.ONCLICK == event.getTypeInt() )
				{
					Element target = event.getTarget();
					String targetId = target.getId();
					if ( targetId.contains( "add" ) )
					{
						simplePopup.setWidget( menuWidget );
						simplePopup.setPopupPosition( addElem.getAbsoluteLeft(), addElem.getAbsoluteTop() );
						simplePopup.show();
					}
				}
			}
		} );
		Element refreshElem = DOM.getElementById( "img-refresh-" + idPrefix );
		DOM.sinkEvents( refreshElem, Event.ONCLICK | Event.ONMOUSEOUT | Event.ONMOUSEOVER );
		DOM.setEventListener( refreshElem, new EventListener()
		{
			public void onBrowserEvent( Event event )
			{
				if ( Event.ONCLICK == event.getTypeInt() )
				{
					Element target = event.getTarget();
					String targetId = target.getId();
					if ( targetId.contains( "refresh" ) )
					{
						simplePopup.setWidget( new HTML( "You can invoke refresh on the Chart." ) );
						simplePopup.setPopupPosition( addElem.getAbsoluteLeft(), addElem.getAbsoluteTop() );
						simplePopup.show();
					}
				}
			}
		} );

		simplePopup.hide();
	}

	public void setMenuWidget( IsWidget menuWidget )
	{
		this.menuWidget = menuWidget;
	}

	public native void addWidget( String id, String textContent )
	/*-{
		var gridster = this.@com.ducksboard.gridster.Gridster::jso;
		var content = '';
		content = content + '<li id="li-' + id + '">';
		content = content + '<span style="float:right; display:block;">';
		content = content
				+ '	<img src="images/icons/bullet_add.png" id="img-add-' + id
				+ '" width="32" height="32">';
		content = content
				+ '	<img src="images/icons/bullet_deny.png" id="img-delete-'
				+ id + '" width="32" height="32">';
		content = content
				+ '	<img src="images/icons/arrow_refresh.png" id="img-refresh-'
				+ id + '" width="32" height="32">';
		content = content + '</span>';
		content = content + '<span style="clear: both;display: block;">'
				+ textContent + '</span>';
		content = content + '</li>';
		gridster.add_widget(content);
	}-*/;

	public native void removeWidgetByIndex( int index )
	/*-{
		var gridster = this.@com.ducksboard.gridster.Gridster::jso;
		gridster.remove_widget($wnd.$('.gridster li').eq(index));
	}-*/;

	public native void removeWidgetById( String id )
	/*-{
		var gridster = this.@com.ducksboard.gridster.Gridster::jso;
		var widget = $wnd.$("#" + id);
		console.log('Removing Start.');
		// if this is commented out then the widgets won't re-arrange
		gridster.remove_widget(widget, function() {
			console.log('Removing Done.');
		});
	}-*/;

	public String getCounter()
	{
		return counter.toString();
	}
}