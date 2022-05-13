$(document).ready(function() {
	createChart();
});

function createChart(){
	
	Ext.application({
	    name: 'Fiddle',

	    launch: function() {

	        Ext.create('Ext.panel.Panel', {
	            renderTo: Ext.getBody(),
	            width: 600,
	            height: 400,
	            layout: 'fit',

	            dockedItems: [{
	                xtype: 'toolbar',
	                items: [{
	                    xtype: 'button',
	                    text: 'Download Chart as PNG',
	                    handler: function(btn, e, eOpts) {
	                        btn.up('panel').down("cartesian").download({
	                            filename: "Redwood City Climate Data Chart"
	                        })
	                    }
	                }, {
	                    xtype: 'button',
	                    text: 'Preview Chart',
	                    handler: function(btn, e, eOpts) {
	                        btn.up('panel').down("cartesian").preview()
	                    }
	                }]
	            }],
	            items: {
	                xtype: 'cartesian',
	                store: {
	                    fields: ['name', 'data1', 'data2', 'data3', 'data4', 'data5'],
	                    data: [{
	                        'name': 'metric one',
	                        'data1': 10,
	                        'data2': 12,
	                        'data3': 14,
	                        'data4': 8,
	                        'data5': 13
	                    }, {
	                        'name': 'metric two',
	                        'data1': 7,
	                        'data2': 8,
	                        'data3': 16,
	                        'data4': 10,
	                        'data5': 3
	                    }, {
	                        'name': 'metric three',
	                        'data1': 5,
	                        'data2': 2,
	                        'data3': 14,
	                        'data4': 12,
	                        'data5': 7
	                    }, {
	                        'name': 'metric four',
	                        'data1': 2,
	                        'data2': 14,
	                        'data3': 6,
	                        'data4': 1,
	                        'data5': 23
	                    }, {
	                        'name': 'metric five',
	                        'data1': 27,
	                        'data2': 38,
	                        'data3': 36,
	                        'data4': 13,
	                        'data5': 33
	                    }]
	                },
	                legend: {
	                    position: 'bottom'
	                },
	                axes: [{
	                    type: 'numeric',
	                    position: 'left',
	                    fields: ['data1'],
	                    title: {
	                        text: 'Sample Values',
	                        fontSize: 15
	                    },
	                    grid: true,
	                    minimum: 0
	                }, {
	                    type: 'category',
	                    position: 'bottom',
	                    fields: ['name'],
	                    title: {
	                        text: 'Sample Values',
	                        fontSize: 15
	                    }
	                }],
	                series: [{
	                    type: 'line',
	                    highlight: {
	                        size: 7,
	                        radius: 7
	                    },
	                    xField: 'name',
	                    yField: 'data1'
	                }, {
	                    type: 'line',
	                    highlight: {
	                        size: 7,
	                        radius: 7
	                    },
	                    xField: 'name',
	                    yField: 'data3',

	                    style: {
	                        lineWidth: 10,
	                        shadowColor: 'rgba(0,0,0,0.5)',
	                        shadowBlur: 10,
	                        lineDash: [7, 3, 3, 3]
	                    }

	                }]
	            }
	        });


	    }
	});
	 
}
