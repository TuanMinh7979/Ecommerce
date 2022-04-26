var attributesObject = {};


$(function () {

    $.ajax({
        type: "get",
        url: "/ajax/categoryHierarchical",
        success: function (data) {
            catHierarchical = data;
            renderCatHierarchical();
        },
        error: function () {
            Swal.fire({
                icon: 'error',
                title: 'Can not load this categories in hierarchical',
                // text: 'Something went wrong!',

            })
        }
    });


    $('.nav-tabs a').on('shown.bs.tab', function (event) {
        var editMode = $(event.target).text();

        if (editMode == "Detail") {

            callAttributeApi(`/admin/category/api/${categoryId}/attributes`);
            $("#addNewAttributeBtn").on("click", function (event) {
                event.preventDefault();
                $("#addModalBtn").click();
            })

            $(".saveAtrsBtn").on("click", function (event) {
                event.preventDefault();
                saveCurrentFormAttribute(this);

            });

            $("#tag_delete_many").on("click", function (event) {
                event.preventDefault();
                let checkboxes = $('tr input[type="checkbox"]:checked');
                for (checkbox of checkboxes) {
                    let keyToDel = $(this).parent().parent().find(".atb-iddel-checkbox").val();
                    delete attributesObject[keyToDel];
                    $(checkbox).closest("tr").remove();
                }
            });
            $(document).on("click", "#saveallchangeBtn", function (event) {
                event.preventDefault();
                saveAllChange(attributesObject, $(this).attr("href"));
            })


            $(document).on("click", '.tag_delete_one', function (event) {
                event.preventDefault();
                let keyToDel = $(this).parent().parent().find(".atb-iddel-checkbox").val();
                // removeAttributeByName(nameToDel, attributesObject);
                delete attributesObject[keyToDel];

                $(this).closest("tr").remove();
            })



        }


    });

})

function renderCatHierarchical() {
    let opts = "";
    catHierarchical.map(function (cat) {

        opts += `<option value="${cat.id}">${cat.name}</option> `

    })
    $(".parent-sel").append(opts);

}


function saveAllChange(data, url) {

    $.ajax({
        type: "post",
        url: url,
        contentType: "application/json",
        data: JSON.stringify(data),

        success: function (res) {
            location.reload();
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

$(document).on("click", ".push", function (event) {

    event.preventDefault();
    pushNewRecord(this)


});
$(document).on("click", ".delete", function (event) {

    event.preventDefault();
    $(this).parent().remove();
});


$(document).on("click", ".editAttributeBtn", function (event) {
    event.preventDefault();
    //load data first
    loadAttributeToForm(this);
    //after that click modal btn
    $("#editModalBtn").click();

})

function renderDataForAttributeTable(data) {
    let rs = "";
    for (let atbi in data) {
        let curAtr = data[atbi];


        rs += `       <tr class="col-12">
                <td class="col-1"><input type="checkbox" class="atb-iddel-checkbox" value="${atbi}"></td>
                <td class="col-5" class="atb-name-inp" ">${curAtr.name}</td>
                <td class="col-3"><i class="atb-active-checkbox fas fa-circle" isactive="${curAtr.active}"></i>  </td>
                  <td class="col-3">
                    <button class="editAttributeBtn btn btn-default" >Edit</button>
                    <a class="btn btn-danger tag_delete_one"  >Delete</a>

                </td>

            </tr>`
    }


    $("#tabledata").html(rs);
    setActiveCheckbox();


}

//COMMON METHOD
function callAttributeApi(url) {

    $.ajax({
        type: "get",
        url: url,
        success: function (data) {


            attributesObject = JSON.parse(data);

            renderDataForAttributeTable(attributesObject);
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


function loadAttributeToForm(editAttributeBtn) {
    let tri = $(editAttributeBtn).parent().parent();
    let triKey = tri.find(".atb-iddel-checkbox").val();
    let triName = tri.find("td:nth-child(2)").text();


    let isfilter = attributesObject[`${triKey}`].filter;

    let isactive = tri.find(".atb-active-checkbox").attr("isactive");
    // let isactive = triActive.attr("isactive");

    let editModal = $("#exampleModalCenter2");
    editModal.find("#atrIdInp").val(triKey);
    editModal.find(".atrNameInp").val(triName);


    editModal.find(".atrActiveChbx").prop("checked", isactive == 1 ? true : false);
    editModal.find(".atrFilterChbx").prop("checked", isfilter == 1 ? true : false);


    let valueList = editModal.find(".tasks");


    let currentAttribute = attributesObject[triKey];

    if (currentAttribute["value"] != undefined) {
        let rs = "";
        currentAttribute["value"].map(function (valuei) {
            rs +=
                `<div class="task">
                <span class="atrValueSpan" class="taskname">
                    ${valuei}
                </span>
                        <button class="delete">
                            <i class="far fa-trash-alt"></i>
                        </button>
                    </div>`;


        });
        valueList.html(rs);
    }
}


function saveCurrentFormAttribute(saveBtn) {


    let data = {}
    let modalContent = $(saveBtn).parent().parent();
    let attributeWrapper = modalContent.find(".attributeWrapper");

    data["name"] = attributeWrapper.find(".atrNameInp").val();
    data["active"] = attributeWrapper.find(".atrActiveChbx").is(":checked") ? 1 : 0;
    data["filter"] = attributeWrapper.find(".atrFilterChbx").is(":checked") ? 1 : 0;
    let valueArr = [];
    attributeWrapper.find(".atrValueSpan").each(function () {
        valueArr.push($(this).text().replace(/\r?\n|\r/g, " ").trim());
    })
    data["value"] = valueArr;

    if (attributeWrapper.find("#atrIdInp").val() !== undefined) {
        //case update
        let keyId = attributeWrapper.find("#atrIdInp").val();
        updateAttribute(keyId, data, attributesObject);
        console.log(data);
    } else {
        //case add new attribute -> check if existByName
        if (existByName(data["name"], attributesObject)) {
            alert(data["name"] + " is exist!");
        } else {
            //add new
            let curId = generateID()
            attributesObject[curId] = data;

        }
    }


    let curModal = saveBtn.closest(".modal");
    $(curModal).modal('hide');
    renderDataForAttributeTable(attributesObject);


}


var generateID = function () {
    // Math.random should be unique because of its seeding algorithm.
    // Convert it to base 36 (numbers + letters), and grab the first 9 characters
    // after the decimal.
    return Math.random().toString(36).substr(2, 3);
};

$("#mainForm").data("changed", false);

$("#mainForm").on("change", function () {
    $(this).data("changed", true);
});
$('#mainForm').on('submit', function () {
    if (!$(this).data("changed")) {
        alert("Nothing changed!");
        return false;
        // Reset variable
    }
    // Do whatever you want here
    // You don't have to prevent submission

});















