var attributesObject = {};
var filterData = {};


$(function () {


    $.ajax({
        type: "get",
        url: "/ajax/hierarchical-category",
        success: function (data) {

            renderCatHierarchical(data);
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

    $("#saveCurFiltersBtn").on("click", function () {
        let filterItemWrappers = [];

        $(".filters-item-checkboxes-wrapper").each(function () {
            let curFilterValue = {};

            let allCbox = $(this).find("input[type='checkbox']:checked");

            let atbiName = $(this).attr("id");
            atbiName = atbiName.slice(0, -10);

            if (allCbox.length == 0) {
                attributesObject[atbiName].filterValue = "";
            } else {

                $(allCbox).each(function () {

                    let optionKey = $(this).attr("id");

                    let optionClientVal = $(this).attr("val");

                    curFilterValue[optionKey] = optionClientVal;
                })

            }

            attributesObject[atbiName].filterValue = curFilterValue;

        })

        $('#exampleModalCenter3').modal('hide');
    })


})

function renderCatHierarchical(data) {
    let opts = "";
    data.map(function (cat) {

        if (cat.id != categoryParentId) {
            opts += `<option value="${cat.id}">${cat.name}</option> `

        } else {
            opts += `<option value="${cat.id}" selected>${cat.name}</option> `
        }


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
                <td class="col-5" class="atb-name-inp" ">${atbi}</td>
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
            if (data !== "" && data !== null && data !== undefined) {
                attributesObject = JSON.parse(data);

                renderDataForAttributeTable(attributesObject);
            } else {

                alert("This category do not have any attribute");
            }

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
    let triKeyName = tri.find(".atb-iddel-checkbox").val();

//
    let isfilter = attributesObject[`${triKeyName}`].filter;
    let isactive = tri.find(".atb-active-checkbox").attr("isactive");


    let editModal = $("#exampleModalCenter2");
    editModal.find(".atrNameInp").val(triKeyName);
    editModal.find(".atrActiveChbx").prop("checked", isactive == 1 ? true : false);
    editModal.find(".atrFilterChbx").prop("checked", isfilter == 1 ? true : false);


    let valueList = editModal.find(".tasks");

    let currentAttribute = attributesObject[triKeyName];

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

    let keyname = attributeWrapper.find(".atrNameInp").val();
    data["active"] = attributeWrapper.find(".atrActiveChbx").is(":checked") ? 1 : 0;
    data["filter"] = attributeWrapper.find(".atrFilterChbx").is(":checked") ? 1 : 0;
    let valueArr = [];
    attributeWrapper.find(".atrValueSpan").each(function () {
        valueArr.push($(this).text().replace(/\r?\n|\r/g, " ").trim());
    })
    data["value"] = valueArr;

    if ($("#exampleModalCenter2").hasClass("show")) {
        updateAttribute(keyname, data, attributesObject);

    } else {
        //case add new attribute -> check if existByName
        if (existByName(keyname, attributesObject)) {
            alert(keyname + " is exist!");
        } else {
            //add new
            attributesObject[keyname] = data;

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

//FOR CUSTOM ATTRIBUTE FILTER
$("#editFilterBtn").on("click", function (event) {

    event.preventDefault();
    loadFilterData();


})


function getAndRenderCheckedFilterOpt(atbKeyNames) {

    $.ajax({
        type: "post",
        url: "/product-filter/list-map",
        data: JSON.stringify(atbKeyNames),
        contentType: "application/json",

        success: function (data) {


            let rs = "";
            atbKeyNames.map(function (atbKeyName) {

                let atbKeyObj = {};
                atbKeyObj = attributesObject[atbKeyName];
                rs += "<div class='filters-item__div'>";
                rs += `<label>${atbKeyName}</label>`;
                rs += `<div id="${atbKeyName}OptWrapper" class="filters-item-checkboxes-wrapper">`

                if (data[atbKeyName] !== undefined) {

                    if (atbKeyObj["filterValue"] === undefined) {
                        atbKeyObj["filterValue"] = {};
                    }
                    let keyClientVal = atbKeyObj.filterValue;
                    for (let optionDataKeyName in data[atbKeyName]) {
                        let dataKeyClientVal = data[atbKeyName];
                        if (keyClientVal.hasOwnProperty(optionDataKeyName)) {
                            rs += `<input id=${optionDataKeyName} class="ml-3" type='checkbox' checked val="${dataKeyClientVal[optionDataKeyName]}"/>  <span>${dataKeyClientVal[optionDataKeyName]}</span>`

                        } else {
                            rs += `<input id=${optionDataKeyName} class="ml-3" type='checkbox' val="${dataKeyClientVal[optionDataKeyName]}" /> <span>${dataKeyClientVal[optionDataKeyName]}</span>`
                        }
                    }
                } else {
                    rs += "<p>System do not have any option for this attribute</p>"
                }


                rs += "</div>"
                rs += "</div>"
            })
            // console.log(attributesObject);


            $("#filterWrapper").html(rs);

        },
        error: function () {
            Swal.fire({
                icon: 'error',
                title: 'Can not load content',
                // text: 'Something went wrong!',

            })
        }
    });


}

function loadFilterData() {
    let atbKeyNames = [];
    for (let atbKeyName in attributesObject) {
        let atbKeyObj = attributesObject[atbKeyName];
        if (atbKeyObj.hasOwnProperty("filter") && atbKeyObj.filter == 1) {
            atbKeyNames.push(atbKeyName);
        }
    }
    if (atbKeyNames.length == 0) {
        alert("There are no attributes that is ticked in it's filter field, please tick one before!")
    } else {
        getAndRenderCheckedFilterOpt(atbKeyNames);
        $("#editFilterModalBtn").click();
    }

}






















