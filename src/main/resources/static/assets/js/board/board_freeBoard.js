$(function (){

    $(".boardView").on("click", function() {

        let boardId = $(this).attr("data-num");
        location.href = "view/" + boardId;
    })
})