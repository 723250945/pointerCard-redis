$(document).ready(function(){
    var result=0;
    var show="";
    var inresult;
    var tag=false;
    getArithmetic();

    /**
     * 改变验证码算术式
     */
    $(document).on("click","#sswitch",function(){
        getArithmetic();
    });

    /**
     *
     */
    $(document).on("blur","#result",function(){
        inresult=$("#result").val();
        if(result!=inresult){
            $("#result").parent().next().html("");
            $("#result").parent().next().html("答案错误");
        }
        if(inresult==""){
            $("#result").parent().next().html("");
            $("#result").parent().next().html("答案不能为空！");
        }
        if(inresult==result){
            $("#result").parent().next().html("");
            $("#result").parent().next().html("验证通过！");
            tag=true;
        }
    });
    /**
     * 生成验证算术式
     */
    function getArithmetic() {
        tag=false;
        var a=parseInt(Math.random()*10);//生成0-10的随机数，可以生成到100增加计算难度
        var b=parseInt(Math.random()*10);
        var n1=Math.floor(Math.random()*3+1);//输出1～4之间的随机整数
        if(n1==1){
            result=a+b;
            show=a+" + "+b+"=";
        }
        if(n1==2){
            result=a-b;
            show=a+" - "+b+"=";
        }
        if(n1==3){
            result=a*b;
            show=a+" * "+b+"=";
        }
      /*  if(n1==4){
            result=a/b;
            show=a+"/"+b+"=";
            alert(result);
        }*/
        $("#show").val(show);
        $("#show").html(show);
    }
});