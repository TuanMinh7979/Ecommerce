
function setActiveCheckbox() {
    $(".atb-active-checkbox").each(function () {

        let isActive = $(this).attr("isActive") == 1 ? true : false;

        if (isActive == 1) {
            $(this).css("color", "blue");
        } else {
            $(this).css("color", "red");
        }


    })
}


function pushNewRecord(pushAtrBtn) {
    let newtask = $(pushAtrBtn).parent();
    let newtaskInput = $(newtask).find("input");
    if (newtaskInput.val().length == 0) {

        alert("Please Enter a Task");
    } else {
        let tasksWrapper = newtask.parent().find(".tasks");
        let rs = "";
        rs += `
            <div class="task">
                <span class="atrValueSpan" class="taskname">
                   ${newtaskInput.val()}
                </span>
                <button class="delete">
                    <i class="far fa-trash-alt"></i>
                </button>
            </div>
        `;
        tasksWrapper.append(rs);

    }
}

function existByName(name, collection) {
    for (let attribute of collection) {

        if (attribute.name === name) {
            return true;
        }
    }
    return false;
}

function findAttributeById(id, collection) {
    for (let atri of collection) {

        if (atri["id"] == id) {
            return atri;
        }
    }
    return;
}

function removeAttributeByName(name, collection) {

    collection.map((x, index) => {
        if (x["name"] === name) {
            collection.splice(index, 1);
            return;
        }
    });


    return;
}

function updateAttribute(newAttribute, collection) {
//diffenrence from java , from java we can update a T in List INDEPENDENT OF List
//bust still affect to LIST
    collection.map((x, index) => {
        if (x["id"] === newAttribute["id"]) {
            collection[index] = newAttribute;
            return;
        }
    });

    return;
}

//
//
//
//
//
//
//
//
