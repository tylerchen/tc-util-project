;
function or_bind_submitable(selector){
	$(selector).bind('click',function(){
		if('_blank'==$(this).attr('target')){
			var link=$('<a>',{
				target: '_blank',
				href  : or_submit_url($(this).closest('form').serialize()+'&'+$(this).attr('href')),
				style : 'display:none'
			});
			link.appendTo("body");
			link[0].click();
			link.remove();
		}else{
			window.location.href=or_submit_url($(this).closest('form').serialize()+'&'+$(this).attr('href'));
		}
		return false;
	});
}
function or_submit_url(condition){
	if(!condition){
		return window.location.href;
	}
	var urlSplit=window.location.href.split('/'), conditionSplit=condition.split('&'), newUrl=[], conditionGroup={};
	for(var c=0; c<conditionSplit.length; c++){// group the conditions for multi value, such as : multi_select=F, multi_select=M -> multi_select=F,M
		var cdn=conditionSplit[c];
		if(cdn.indexOf('=')<0){
			continue;
		}
		var name=cdn.substring(0,cdn.indexOf('=')+1), value=cdn.substring(cdn.indexOf('=')+1);
		(conditionGroup[name]=conditionGroup[name]||[]).push(value);
	}
	conditionSplit=[];
	for(var p in conditionGroup){
		conditionSplit.push(p+conditionGroup[p].join(','));
	}
	for(var c=0; c<conditionSplit.length; c++){
		var cdn=conditionSplit[c];
		var name=cdn.substring(0,cdn.indexOf('=')+1), hasCnd=false;
		for(var i=0; i<urlSplit.length; i++){
			if(urlSplit[i].startsWith(name)){
				urlSplit[i]=(hasCnd ? null : cdn);
				hasCnd=true;
			}
		}
		if(!hasCnd){
			urlSplit.push(cdn);
		}
	}
	for(var i=0; i<urlSplit.length; i++){
		if(urlSplit[i]!==null){
			newUrl.push(urlSplit[i]);
		}
	}
	return newUrl.join('/');
}
function or_url_param_fill() {
	var href = window.location.href;
	var split = href.split('/');
	var paramMap = {};
	for ( var i = 0; i < split.length; i++) {
		var p = split[i];
		if (p.indexOf('=') < 0) {
			continue;
		}
		var index = p.indexOf('=');
		if (index > 0) {
			paramMap[p.substring(0, index)] = p.substring(index + 1);
		}
	}
	for (var p in paramMap) {
		var el = $("form :input[name='" + p + "']:first");
		if (!el.length) {
			continue;
		}
		var val=decodeURI(paramMap[p]);
		if (el.is('input')) {
			if(el.closest('.ms-parent').length<1){
				el.val(val);
			}
		} else if (el.is('select')) {
			if(el.attr('multiple')=='multiple'){
				el.multipleSelect('setSelects', (val||'').split(','));
			}else{
				$('option', el).filter(function() {
					return $(this).val() == val;
				}).prop('selected', true);
				el.trigger('change');
			}
		}
	}
}