var GlobalTotalPage = 1;//flag variable
var currentPage = 1;
var customerName = "";
var filterringFlag = 0;

callViewApi();

function callViewApi(page, limit, sortBy, sortDirection, fromDate, toDate, status) {

    let url = "/admin/transaction/api/viewApi";
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
    if (fromDate != null && fromDate != "") {
        url += `&fromDate=${fromDate}`;
    }
    if (toDate != null && toDate != "") {
        url += `&toDate=${toDate}`;
    }
    if (status != null) {
        url += `&status=${status}`;
    }
    console.log(url);


    $.ajax({
        type: "get",
        url: url,
        success: function (data) {
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

function validateDatePicker(fromDate, toDate) {
    var fromDateObj = new Date(fromDate);
    var toDateObj = new Date(toDate);
    if (fromDateObj.getTime() >= toDateObj.getTime()) {
        return true;
    }
    return false;

}

function HdleFilterBtn() {
    filterringFlag = 1;
    //sort
    let sortBy = document.getElementById("by-sel").value;
    let sortDirection = document.getElementById("direction-sel").value;
    let limit = document.getElementById("limit-inp").value;
    //sort

    //filter
    let status = document.getElementById("status-sel").value;
    let fromDate = document.getElementById("fromDatepicker").value;
    let toDate = document.getElementById("toDatepicker").value;
    if (validateDatePicker(fromDate, toDate)){
        $("input[type=date]").val("")
        alert("Invalid choosed time, please choose again!");
    } else {
        callViewApi(currentPage, limit, sortBy, sortDirection, fromDate, toDate, status);
    }

    //filter


}

function renderData(data) {
    let rs = "";
    data.data.map(function (trai) {
            rs += `    <tr>
      <td class="col-1">${trai.id}</td>
      <td class="col-2">${trai.customerName}</td>
      <td class="col-3">${trai.customerAddress}</td>
      <td class="col-2">${trai.createAt}</td>
      <td class="col-1">${trai.totalPrice}</td>
      <td class="col-1">${trai.status}</td>
      <td class="col-2">
      <a class="btn btn-default"  href="/admin/transaction/update/${trai.id}">Edit</a>
      <a class="btn btn-danger"  href="/admin/transaction/api/delete${trai.id}">Delete</a>
      
      </td>
    </tr>`
        }
    )

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

                    //filter
                    let status = document.getElementById("status-sel").value;
                    let fromDate = document.getElementById("fromDatepicker").value;
                    let toDate = document.getElementById("toDatepicker").value;

                    //filter
                    if (filterringFlag == 0) {
                        callViewApi(page, limit, sortBy, sortDirection, fromDate, toDate, status);

                    } else {
                        filterringFlag = 0;
                    }
                }
            }
        );
    }


}


$("#tag_delete_many").on("click", deleteManyOnTable);
$("#btn-filter").on("click", HdleFilterBtn);
$(document).on("click", '.tag_delete_one', deleteOnTable);
$("#customername-search-inp").on("change", function () {
    customerName = $(this).val();
})
$("#customername-search-btn").on("click", function () {
    console.log("");
})


//check form function











