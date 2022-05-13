(function($){
	$(function(){
		if (!$('#easyui-color-style').length){
			$('head').append(
				'<style id="easyui-color-style">' +
				'.color-cell{display:inline-block;float:left;cursor:pointer;}' +				
				'.color-cell-border{border:1px solid #fff}' +
				'.color-cell-border:hover{border:1px solid #000}' +
				'.color-cell-noborder{border:1px solid #f29535; background:url(/resources/images/slash.png); background-size:100% 100%;}' +
				'</style>'
			);
		}
	});

	function create(target){
		var opts = $.data(target, 'color').options;
		$(target).combo($.extend({}, opts, {
			panelWidth: opts.cellWidth*10+2,
			panelHeight: opts.cellHeight*7+2,
			onShowPanel: function(){
				var p = $(this).combo('panel');
				if (p.is(':empty')){
					for(var i = 0; i < colorValArry.length ; i++){
						var a = "";
						if(colorValArry[i] != null){
							a = $('<a class="color-cell color-cell-border" onClick="setRgbColorId(\''+colorIdArry[i]+'\');"></a>').appendTo(p);	
						}else{
							a = $('<a class="color-cell color-cell-noborder" onClick="setRgbColorId(\'N\');"></a>').appendTo(p);
						}
						
						a.css('background-color', colorValArry[i]);
					}
					
					var cells = p.find('.color-cell');
					cells._outerWidth(opts.cellWidth)._outerHeight(opts.cellHeight);
					cells.bind('click.color', function(e){
						var color = $(this).css('backgroundColor');
						$(target).color('setValue', color);
						$(target).combo('hidePanel');
					});
				}
			}
		}));
		if (opts.value){
			$(target).color('setValue', opts.value);
		}
	}

	$.fn.color = function(options, param){
		if (typeof options == 'string'){
			var method = $.fn.color.methods[options];
			if (method){
				return method(this, param);
			} else {
				return this.combo(options, param);
			}
		}
		options = options || {};
		return this.each(function(){
			var state = $.data(this, 'color');
			if (state){
				$.extend(state.options, options);
			} else {
				state = $.data(this, 'color', {
					options: $.extend({}, $.fn.color.defaults, $.fn.color.parseOptions(this), options)
				});
			}
			create(this);
		});
	};

	$.fn.color.methods = {
		options: function(jq){
			return jq.data('color').options;
		},
		setValue: function(jq, value){
			return jq.each(function(){
				var tb = $(this).combo('textbox').css('backgroundColor', value);
				value = tb.css('backgroundColor');
				var mixer = "";

				if(value != "rgba(0, 0, 0, 0)"){ // backgroundColor 가 없는 경우...
					if (value.indexOf('rgb') >= 0){
						var bg = value.match(/^rgb\((\d+),\s*(\d+),\s*(\d+)\)$/);
						value = '#' + hex(bg[1]) + hex(bg[2]) + hex(bg[3]);
						mixer = hex(bg[1]) + hex(bg[2]) + hex(bg[3]);
					}
					$(this).combo('setValue', value).combo('setText', set_uppercase(value));
					var changeColor = complementary(set_uppercase(mixer));
	
					$(this).combo('textbox').css('color','#'+changeColor);
				}
				
				function hex(x){
					return ('0'+parseInt(x).toString(16)).slice(-2);
				}
				
				function set_uppercase(field){
					return field.toUpperCase();
				}
				
				function complementary(color){
					var result = "";
					var ch = "";
					var list1 = "0123456789ABCDEF";
					var list2 = "FEDCBA9876543210";
					
					for(var i = 0 ; i < color.length ; i++){
						ch = color.charAt(i);
						for(var n = 0 ; n < list1.length ; n++){
							if(ch == list1.charAt(n)){
								result += list2.charAt(n);
							}
						}
					}
					
					return result;
				} 
			})
		},
		clear: function(jq){
			return jq.each(function(){
				$(this).combo('clear');
				$(this).combo('textbox').css('backgroundColor', '');
			});
		}
	};

	$.fn.color.parseOptions = function(target){
		return $.extend({}, $.fn.combo.parseOptions(target), {

		});
	};

	$.fn.color.defaults = $.extend({}, $.fn.combo.defaults, {
		editable: false,
		cellWidth: 20,
		cellHeight: 20
	});

	$.parser.plugins.push('color');
})(jQuery);
