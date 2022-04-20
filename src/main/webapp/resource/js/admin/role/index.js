var GlobalTotalPage = 1;//flag variable
var currentPage = 1;
var activeKeyword = "";


function callViewApi(page, limit, sortBy, sortDirection, searchNameTerm) {

    let url = "/admin/role/api/viewApi";
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

    callViewApi(currentPage, limit, sortBy, sortDirection, activeKeyword);
}

function renderData(data) {
    let rs = "";


    data.data.map(function (rolei) {
            rs += "<tr>"
            rs += `<td class="col-1"><input type="checkbox" value="${rolei.id}"></td>`
            rs += `<td class="col-4" >${rolei.name}</td>`
            rs += `<td class = "col-4" >${rolei.description} </td>`
            rs += '<td class="col-3">'
            rs += `<a class="btn btn-default"  href="/admin/role/edit/${rolei.id}">Edit</a>`
            rs += `<a class="btn btn-danger tag_delete_one"  href="/admin/role/api/delete/${rolei.id}">Delete</a>`
            rs += "</td>"
            rs += "</tr>"


        }
    );
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


                    callViewApi(page, limit, sortBy, sortDirection, activeKeyword);

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

//Use event delegation for dynamically created elements(bind event on ajax loaded content)

//Use event delegation for dynamically created elements:


//autocomplete
$(function () {
    $("#main-search-inp").on("change", function () {
        activeKeyword = $(this).val();


    })


    $("#main-search-inp").autocomplete({
        source: "/ajax/autocomplete-search/role",

        //can not fail
        select: function (event, ui) {
            window.location.href = "/admin/role/edit/" + ui.item.value;

        }
    })
    $("#main-search-btn").on("click", function () {
        callViewApi(currentPage, null, null, null, activeKeyword);

    })
})









