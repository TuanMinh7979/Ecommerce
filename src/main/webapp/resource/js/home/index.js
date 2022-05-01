$(function () {

    $('#select').on('change', function () {
        if ($(this).val() === 'important') {
            $(this).addClass('red')
        }
    })
    $(".list-sort button").on("click", function (event) {
        event.preventDefault();
        $(".list-sort button").removeClass("active");
        $(this).toggleClass("active");
    })

    loadTreeMenu("/ajax/menudata");

})

function categoryClick(event) {
    let divTag = event.target;
    event.stopPropagation()
    let liTag = $(divTag).closest("li");
    let url = $(liTag).attr("catlink");
    window.location.href = url;
}

function loadTreeMenu(url) {
    $.ajax({
        type: "get",
        url: url,
        success: function (data) {


            let arrData = []
            arrData = data;
            let source = builddata(arrData);

            let ul = $("<ul></ul>");
            $("#jqxMenu").append(ul)
            buildUL(ul, source);

            $("#jqxMenu").jqxTree();


        },
        error: function () {
            Swal.fire({
                icon: 'error',
                title: 'Can not load menu',
                // text: 'Something went wrong!',

            })
        }


    });

}










