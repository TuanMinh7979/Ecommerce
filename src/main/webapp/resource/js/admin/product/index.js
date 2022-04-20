var GlobalTotalPage = 1;//flag variable
var currentPage = 1;
var activeKeyword = "";
var category = "";

function callViewApi(page, limit, sortBy, sortDirection, searchNameTerm, category) {

    let url = "/admin/product/api/viewApi";
    if (page == null) {
        url += `?page=1`;
    } else {
        url += `?page=${page}`;
    }
    if (limit != null) {
        url += `&limit=${limit}`;
    }
    if (sortBy != null) {
        url += `&sortBy=${sortBy}`;
    }
    if (sortDirection != null) {
        url += `&sortDirection=${sortDirection}`;
    }
    if (searchNameTerm != null) {
        url += `&searchNameTerm=${searchNameTerm}`;
    }
    if (category != null) {
        url +=`&category=${category}`;
    }


    $.ajax({
        type: "get",
        url: url,
        success: function (data) {
            console.log(data.data)
            renderData(data);
        },
        error: function () {
            Swal.fire({
                icon: 'error',
                title: 'Can not call this Api',
                // text: 'Something went wrong!',

            })
        }


    });
}

$(function () {
    //khi tai lai trang thi se call data va paging lai
    callViewApi();

})


function HdleFilterBtn() {
    //load data sử dụng bộ filter
    let sortBy = document.getElementById("by-sel").value;
    let sortDirection = document.getElementById("direction-sel").value;
    let limit = document.getElementById("limit-inp").value;

    callViewApi(currentPage, limit, sortBy, sortDirection, activeKeyword, category);


}

function renderData(data) {
    let rs = "";
    console.log(data.data);
    data.data.map(function(producti) {
        rs += '<tr>'
        rs += `<td class="col-1"><input type="checkbox" value="${producti.id}"></td>`

        rs += `<td class="col-2" >${producti.name}</td>`
        rs += `<td class = "col-1" >${producti.price} </td>`
        rs += `<td class = "avatar-container col-3" ><img class="avatar__img"src=${producti.mainImageLink} alt="No Image choosed"/> </td>`
        rs += `<td class="col-2"> ${producti.code}</td>`
        rs += `<td class="col-1"> ${producti.category.name}</td>`

        rs += '<td class="col-2">'
        rs += `<a class="btn btn-default"  href="/admin/product/edit/${producti.id}">Edit</a>`
        rs += `<a class="btn btn-danger tag_delete_one"  href="/admin/product/api/delete/${producti.id}">Delete</a>`
        rs += "</td>"
        rs += "</tr>"


    });
    $("#tabledata").html(rs);
    let newTotalPage = data.totalPage;
    if (newTotalPage != GlobalTotalPage) {

        GlobalTotalPage = newTotalPage;
        var $pagination = $("#pagination-demo");
        $pagination.twbsPagination("destroy");
        $pagination.twbsPagination({
                totalPages: GlobalTotalPage,
                visiblePages: GlobalTotalPage,
                onPageClick: function (event, page) {

                    currentPage = page;
                    let sortBy = document.getElementById("by-sel").value;
                    let sortDirection = document.getElementById("direction-sel").value;
                    let limit = document.getElementById("limit-inp").value;


                    callViewApi(page, limit, sortBy, sortDirection, activeKeyword, category);

                }
            }
        );
    }


}

$(function () {

    //doi voi preventDefault nhu the a nên đặt onclick vì khi html render bằng ajax event bằng class hay id sẽ KHÔNG đc

    $("#tag_delete_many").on("click", deleteManyOnTable);
    $("#btn-filter").on("click", HdleFilterBtn);
    $(document).on("click", '.tag_delete_one', deleteOnTable);

})



//autocomplete
$(function () {
    $("#main-search-inp").on("change", function () {
        activeKeyword = $(this).val();


    })

    $("#category-sel").on("change", function () {
        category = $(this).val();


    })

    $("#main-search-inp").autocomplete({
        source: "/ajax/autocomplete-search/product",

        //can not fail
        select: function (event, ui) {
            window.location.href = "/admin/product/edit/" + ui.item.value;

        }
    })
    $("#main-search-btn").on("click", function () {
        callViewApi(currentPage, null, null, null, activeKeyword, category);

    })
})









