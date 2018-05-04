;
if(!String.prototype.endsWith){
	String.prototype.endsWith = function(suffix) {
	    return this.indexOf(suffix, this.length - suffix.length) !== -1;
	};
}
if(!String.prototype.startsWith){
	String.prototype.startsWith = function(suffix) {
		return this.indexOf(suffix) === 0;
	};
}
if(!String.prototype.trim){
	String.prototype.trim = function() {
		return this.replace(/(^\s*)|(\s*$)/g, '');
	};
}
function or_bind_submitable(selector){
	$(selector).on('click',function(event){//event.stopPropagation();
		event.preventDefault(); var el=$(this), target=el.attr('target'), type=el.attr('type'), href=el.attr('href'), form=el.closest('form');
		if('_blank'==target){
			var link=$('<a>',{
				target: '_blank',
				href  : or_submit_url(form, 'submit'==type?('p=0&'+href):href),
				style : 'display:none'
			});
			try{console.log(link.attr('href'))}catch(err){}
			link.appendTo("body");
			link[0].click();
			link.remove();
		}else{
			var href=or_submit_url(form, 'submit'==type?('p=0&'+href):href);
			try{console.log(href)}catch(err){}
			window.location.href=href;
		}
		return false;
	});
}
function or_submit_url(form, condition){
	var urlSplit=window.location.href.split('/'), content=form.serialize(), conditionSplit=content.split('&'), conditionMap={}, contentMap={};
	for(var i=0;i<conditionSplit.length;i++){
		var cdt=conditionSplit[i];
		if(cdt.indexOf('=')>-1){
			conditionMap[cdt.substring(0,cdt.indexOf('='))]=cdt.substring(cdt.indexOf('=')+1);
		}
	}
	$(':input', form).each(function(){
		var el=$(this), name=el.attr('name'), val=el.val(), value=(val?(val+''):'').trim(), cdtVal=conditionMap[name];
		if(cdtVal && value && value.length>0){
			if($(".ms-parent :input[name='" + name + "']:first").length<1){
				contentMap[name]=encodeURIComponent(value);
			}
		}
	});
	{
		conditionSplit=(condition||'').split('&');
		for(var i=0;i<conditionSplit.length;i++){
			var cdt=conditionSplit[i];
			if(cdt.indexOf('=')>-1){
				contentMap[cdt.substring(0,cdt.indexOf('='))]=cdt.substring(cdt.indexOf('=')+1);
			}
		}
	}
	for(var i=0; i<urlSplit.length; i++){
		if(urlSplit[i].indexOf('=')>-1){
			urlSplit=urlSplit.slice(0, i);
			break;
		}
	}
	for(var p in contentMap){
		urlSplit.push(p+'='+contentMap[p]);
	}
	return urlSplit.join('/');
}
function or_url_param_fill() {
	var href = window.location.href;
	var split = href.split('/');
	var paramMap = {};
	{// clean url
		var tmpUrlSplit={}, pos=0, starCopy=0;
		for(var i=0; i<split.length; i++){
			var url=split[i];
			if(url.indexOf('=')<0){pos++; continue;}
			starCopy=(starCopy>0 ? starCopy : pos);
			if(url.endsWith('=')){continue;}
			var name=url.substring(0,url.indexOf('=')+1), value=url.substring(url.indexOf('=')+1);
			tmpUrlSplit[name]=value;
		}
		split=split.slice(0, pos);
		for(var p in tmpUrlSplit){
			split.push(p+tmpUrlSplit[p]);
		}
	}
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
		var val=decodeURIComponent(paramMap[p]);
		if(el.closest('.ms-parent').length>0){
			continue;
		}
		if (el.is('input')) {
			el.val(val);
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
function or_select_init(select){
	var el=select?$(select):$('select.or-hw-select');
	el.each(function(){
		$(this).on('change mychange',function(event){
			var sel=$(this), option1=this.options[this.selectedIndex];
			$(sel.attr('id')+'_i').val(option1.text);
		});
	});
}
function or_city_select_init(select1){
	var el=select1?$(select1):$('select.or-hw-select.province');
	el.each(function(){
		$(this).on('change',function(event){
			var sel=$(this), option1=this.options[this.selectedIndex], sel2=$('select.city :first', sel.parent());
			$(sel.attr('id')+'_i').val(option1.text);
			var data=option1.getAttribute('data'), city=(data||'').trim().split(',');
			while(sel2[0].firstChild) {
				sel2[0].removeChild(sel2[0].firstChild);
			}
			var html='<option></option>';
			for(var i=0;i<city.length;i++){
				var cityIdName=city[i], cIdName=[cityIdName, cityIdName];
				cIdName = cityIdName.indexOf(':')>0 ? cIdName=cityIdName.split(':') : cIdName;
				html+=('<option value=\''+cIdName[0]+'\'>'+cIdName[1]+'</option>');
			}
			sel2.append(html);
			$(sel2.attr('id')+'_i').val('');
		});
	});
}
			
			
			
			
			
			
			
			
			
			