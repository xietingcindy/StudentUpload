		//数据获取	
		var goTo = cacheData();

		function getData(page) {
			var page = page;
			$(function() {
				$.ajax({
					type: "get", //提交方式，也可以是get
					url: "StudentServlet", //请求的url地址
					dataType: "JSON", //返回格式为json,也可以不添加这个属性，也可以是（xml、json、script 或 html）。
					async: false, //请求是否异步，默认为异步，这也是ajax重要特性
					data:{
						page: page,
						limit: 9,						
						method:"findAll"
					
					}, //参数值 ,id是定义的要传的参数变量名，后台接收一致，value是页面取的传值的变量名，如果有多个，按照格式每组用逗号隔开，没有参数可以不用
					
					success: function(doc) {
						//var data=JSON.parse(doc);
						goTo.set(page, doc);
						renderData(doc);
						//console.log(data);
					},
					complete: function(id) {
						//请求完成的处理 ，如果没有，可以不用
						
					},
					error: function() {
						alert("出错了，没取到啊!");
					}
				});
			});
		};

		var oUl = document.querySelector("#oul");
		var index = 1;
		oUl.addEventListener("click", function(e) {
			var e = e || window.event;
			if(e.target.tagName.toLowerCase() === "a") {

				//getData(e.target.innerText);
				//goTo.get(e.target.innerText);
				if(!isNaN(e.target.innerText)) {
					index = e.target.innerText;
					goTo.get(index); //拿到数据

				} else if(e.target.innerText == "上一页") {
					index--;
					if(index < 1) {
						index = 1;
					}
					goTo.get(index);
				} else if(e.target.innerText == "下一页") {
					index++;
					goTo.get(index);
				}
			}
		});

		//数据缓存
		function cacheData() {
			var cache = {};
			return {
				set: function(page, doc) {
					cache[page] = doc; //存储已知数据
				},
				get: function(page) {
					if(page in cache) //当前缓存池存在数据
					{
						//console.log(cache[page]);
						renderData(cache[page]);
						//console.log('页面第${page}的数据已经缓存，无需再次请求');
					} //读取数据
					else {
						getData(page);

					}
					return cache[page];
				}
			}
		};
		//显示作品	
		function renderData(doc) {
			var str = '';
			doc.forEach(function(dataDoc) {
				str += `<div class="col-md-4">
							<div class="thumbnail">
								<a href="studentSingle.html?wid=${dataDoc.wid}">
									<img onerror="this.src='img/error.png'" alt="暂无图片，已显示默认图片" src="${dataDoc.picture}" />
								</a>
								<div class="caption">
									<h3>
										${dataDoc.wname}
									</h3>
									<p>
										${dataDoc.introduce}
										
									</p>
									<p>
										 <a class="btn" name="praise" href="javascript:void(0)" onclick="praise(${dataDoc.wid})"><i class="layui-icon layui-icon-praise">赞</i></a> 
										 <a class="btn" name="warm" href="javascript:void(0)" onclick="warm(${dataDoc.wid})"><i class="layui-icon layui-icon-tread">踩</i></a>
										 <a class="btn" name="upload" href="${dataDoc.address}" download="${dataDoc.address}"><i class="layui-icon layui-icon-download-circle">下载</i></a>
									</p>
								</div>	
							</div>
						</div>
		`;

			});
			document.querySelector("#row").innerHTML = str;
		};

	function getDataLeft(page) {
			var page = page || 1;
			$(function() {
				$.ajax({
					async: false, //请求是否异步，默认为异步，这也是ajax重要特性
					type: "get", //提交方式，也可以是get
					url: "StudentServlet", //请求的url地址
					dataType: "json",  //返回格式为json,也可以不添加这个属性，也可以是（xml、json、script 或 html）。
					
					data: {
						page: page,
						limit: 4,
						method:"findByPraise"
					}, //参数值 ,id是定义的要传的参数变量名，后台接收一致，value是页面取的传值的变量名，如果有多个，按照格式每组用逗号隔开，没有参数可以不用
					beforeSend: function() {
						//请求前的处理，如果没有可以不用写
					},
					success: function(doc) {
						//var data=JSON.parse(doc);
						
						renderDataLeft(doc);
						//console.log(data);
					},
					complete: function(id) {
						//请求完成的处理 ，如果没有，可以不用
						
  						
					},
					error: function() {
						alert("出错了，没取到啊!");
					}
				});
			});
		};
	//最具人气作品渲染	
		function renderDataLeft(doc) {
			var str = '';
			doc.forEach(function(dataDoc) {
				str += `<div>
					<a href="studentSingle.html?wid=${dataDoc.wid}">
						<img onerror="this.src='img/error.png'" alt="暂无图片，已显示默认图片" src="${dataDoc.picture}" class="img-thumbnail pull-left" />
					</a>	
						<button type="button" onclick="praise(${dataDoc.wid})" class="btn btn-default" id="btgood">
          				  	<span class="glyphicon glyphicon-thumbs-up"></span> 为它点赞
        				</button>
        				<div class="layui-anim" data-anim="layui-anim-up">+1</div>
						<div class="code">layui-anim-up</div>
        				<label id="hot">当前人气：${dataDoc.praise}</label>
					
				</div>
		`;

			});
			document.querySelector("#dataleft").innerHTML = str;
		};

		
		
		
//搜索框功能
var page=page;
var goTo = cacheData();
$("#isearch").keydown(function(event){
	if(event.keyCode == 13){
     search1(page);
     return false;
	}
});


//搜索功能,提交ajax数据到后台
function search1(page){
	var page=page;
    var key =  $("#isearch").val();
   
    //var datas = {'search': key};
    $.ajax({
    	async:true,
        url: "StudentServlet",
        type: 'post',
        dataType: "json",
        data: {
        	wname:key,
        	page:page,
        	limit:9,
        	method:"findByName"
        },
        
        
        success: function (data) {
            
               	renderData(data);
                
            	goTo.set(page, data);
            }
        
    });
};




//按类型搜索

var type;
var typeval = document.querySelector("#breadbtn");
		typeval.addEventListener("click", function(e) {
			var e = e || window.event;
			if(e.target.tagName.toLowerCase() === "a") {
				 //console.log(e.target.innerHTML);
				 type=e.target.innerHTML;
				 searchBytype(1);
			}
		});
						
var page=page;
var goTo = cacheData();	
function searchBytype(page){
	
	 $.ajax({
        url: "StudentServlet",
        data: {
        	type:type,
        	page:page,
        	limit:9,
        	method:"findByType"
        },
        type: 'post',
        dataType: 'json',
        success: function (data) {
               	renderData(data);
            	goTo.set(page, data);
            }
        
    });
};


//提交表单搜索，当最后一个表单失焦后提交

		
$("#wname").keydown(function(event){
	if(event.keyCode == 13){
     fsearch(1);
     return false;
	}
});	

var page=page;
function fsearch(page){
	var tname = $("#tname").val();
	var sname = $("#sname").val();
	var wname = $("#wname").val();
	if(tname.length==0||sname.length==0||wname.length==0){	
		return false;
	}
	else{
		$.ajax({
			type:"POST",
			url : "StudentServlet",
			dataType:"json",
			data : {
			tname:tname,
			sname:sname,
			wname:wname,
			limit:9,
			page:page,
			method:"findByCompose"
			
			},
			success : function(doc) {
				if(doc!=null){
					renderData(doc);
					goTo.set(page, doc);
				}
				else{
					
				}
			}
			
		});
			
		};
	}
	
		
	




       
       
			  

	