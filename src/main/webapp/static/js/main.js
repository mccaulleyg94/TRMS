let baseUrl = '/_Project_1__TRMS_war';
let nav = document.getElementById('navBar');
let mainNav = document.getElementById('mainNav');
let secondaryNav = document.getElementById('secondaryNav');
let loggedUser = null;
setNav();

function setNav() {
    secondaryNav.innerHTML = "";
    nav.innerHTML = `
            <strong>Tuition Reimbursement <br> Management System</strong>`;
    if (!loggedUser) {
        nav.innerHTML += `
            <form>
                <label for="username">Username: </label>
                <input id="username" name="username" type="text" />
                <label for="password"> Password: </label>
                <input id="password" name="password" type="password" />
                <button type="button" id="loginBtn">Log In</button>
            </form>
            <button type="button" id="register">Register</button>
        `;

        let registerBtn = document.getElementById('register');
        registerBtn.onclick = employeeRegisterMenu;
    } else {
        nav.innerHTML += `
            <span>
                Logged in as: ${loggedUser.username} | Id: ${loggedUser.employeeId} | Rank: ${loggedUser.rank.rankName} | Department: ${loggedUser.department.departmentName}
                <button type="button" id="loginBtn">Log Out</button>
            </span>
            
            <span>
                <button type="button" id="rrRequest">Submit a Reimbursement Request</button>
            </span>
            
            <span> 
            <button type="button" id="reviewBtn">Review Reimbursement Requests</button> 
            </span> 

            <span> 
            <button type="button" id="rrStatus">Check My Reimbursement Request Status(s)</button> 
            </span> 
            
            <span> 
            <button type="button" id="emailBtn">Email</button> 
            </span> 
        `;

        let rrStatusBtn = document.getElementById('rrStatus');
        rrStatusBtn.onclick = viewYourRequests;

        let rrRequestBtn = document.getElementById('rrRequest');
        rrRequestBtn.onclick = registerRequestMenu;

        let reviewBtn = document.getElementById('reviewBtn');
        reviewBtn.onclick = reviewRequests;
        reviewBtn.hidden = loggedUser.rank.rankId <= 0;

        let emailBtn = document.getElementById('emailBtn');
        emailBtn.onclick = emailMenu;
    }

    let loginBtn = document.getElementById('loginBtn');
    if (loggedUser) loginBtn.onclick = logout;
    else loginBtn.onclick = login;
}

async function emailMenu() {
    secondaryNav.innerHTML = "";
    setNav();
    let url = baseUrl + "/email/check?";

    url += 'rId=' + loggedUser.employeeId;

    mainNav.innerHTML = "";

    console.log(url);
    let response = await fetch(url, {method: 'GET'});

    if (response.status === 200) {
        let Emails = await response.json();


        let table = document.createElement('table');
        table.setAttribute('classname', 'tabledata')

        if (Emails.length > 0) {
            table.innerHTML = `
                <tr>
                    <th>Title</th>
                    <th>Sender</th>
                    <th>Sent Time</th>
                </tr>
            `;

            for (let email of Emails) {
                let tr = document.createElement('tr');
                tr.innerHTML = `
                    <td>${email.title}</td>
                    <td>${email.sender.username}</td>
                    <td>${email.sentTime.month.substring(0, 3)} 
                        ${email.sentTime.dayOfMonth}, 
                        ${email.sentTime.year}
                        ${email.sentTime.hour}
                        ${email.sentTime.minute}</td>
                `;

                let openBtn = document.createElement('button');
                openBtn.type = 'button';
                openBtn.id = email.recipient.employeeId + '_' + email.emailId;
                openBtn.textContent = 'Open Email';

                openBtn.addEventListener('click', checkEmail);

                tr.appendChild(openBtn);
                table.appendChild(tr);
            }
            mainNav.appendChild(table);
        } else {
            mainNav.innerHTML = "You do not have any email records in our system."
        }

        let div = document.createElement('div');

        let sendBtn = document.createElement('button');
        sendBtn.type = 'button';
        sendBtn.id = loggedUser.employeeId;
        sendBtn.textContent = 'Send Email';
        sendBtn.addEventListener('click', sendEmailView);
        div.appendChild(sendBtn);

        mainNav.appendChild(div);
    } else {
        mainNav.innerHTML = "THERE WAS AN ERROR RETRIEVING INFORMATION FROM THE SERVER."
    }
}

function sendEmailView() {
    secondaryNav.innerHTML = "";
    mainNav.innerHTML = `
        <input type="text" id="title" placeholder="title" required> <br><br>
        <input type="text" id="rId" placeholder="recipient id" required> <br><br>
        <input type="text" id="context" placeholder="body" width="30px" height="100px" required> 
        
        <button type="button" id="sendEmail">Send Email</button>
    `;
    let sendEmailBtn = document.getElementById("sendEmail");
    sendEmailBtn.onclick = sendEmail;
}

async function sendEmail() {
    url = baseUrl + '/email/send?';

    url += 'sender=' + loggedUser.employeeId;
    url += '&recipient=' + document.getElementById("rId").value;
    url += '&title=' + document.getElementById("title").value;
    url += '&context=' + document.getElementById("context").value;

    console.log(url);

    let response = await fetch (url, {method: 'POST'});

    if (response.status === 200) {
        email = await response.json();
        mainNav.innerText = "Email was successfully sent to " + email.recipient.username;
        setTimeout(function () {
            setNav();
            mainNav.innerHTML = "";
        }, 3000);
    } else {
        alert("Something went wrong");
    }
}

async function checkEmail() {
    let url = baseUrl + '/email/open?';

    let btnId = event.target.id;
    let index = btnId.indexOf('_')
    let id = btnId.slice(index + 1)

    url += 'emailId=' + id;
    console.log(url);
    let response = await fetch(url, {method: 'GET'});
    if (response.status === 200) {
        let email = await response.json();
        secondaryNav.innerHTML = `
            <h2>${email.title}</h2>
            <p>${email.context}</p> `;
    } else {
        alert("Something went wrong");
    }
}

function registerRequestMenu() {
    setNav();
    mainNav.innerHTML = "";
    mainNav.innerHTML = `
        Submit this form, ${loggedUser.username}! 
        <form>
            <li>COST</li>
            <input type="number" id="cost" required><br><br>
            <li>EVENT TYPE</li>
            <select id="type">
            <option value="1">University Course</option>
            <option value="2">Seminars</option>
            <option value="3">Certification Prep Course</option>
            <option value="4">Certification</option>
            <option value="5">Technical Training</option>
            <option value="6">Other</option>
            </select><br><br>
            <li>TIME OFF START DATE</li>
            <input type="datetime-local" id="startdate"><br><br>
            <li>TIME OFF END DATE</li>
            <input type="datetime-local" id="enddate"><br><br>
            <li>LOCATION</li>
            <input type="text" id="location" required><br><br>
            <input type="text" id="justification"><br><br>
            <input type="file" name=file id="file" value="upload"><br><br>
            <input type="text" id="projected" readonly placeholder="Projected Reimbursement Amount">
            <button type='button' id="projectedBtn">Projected Reimbursement</button> <br><br>
            <button type='reset'>Reset</button> <br><br>
            <button type="button" id="RRregisterBtn">Register</button></li><br><br>
        </form>
    `;
    let RRregisterBtn = document.getElementById('RRregisterBtn');
    RRregisterBtn.onclick = registerRR;
    let projectedBtn = document.getElementById("projectedBtn");
    projectedBtn.onclick = projectedRR;
}

async function projectedRR() {
    let url = baseUrl + '/reimbursement/projected?'

    let type = document.getElementById("type").value;
    let cost = document.getElementById("cost").value;

    url += 'type=' + type + '&cost=' + cost;

    console.log(url);

    let response = await fetch(url);

    if (response.status === 200) {
        let amount = await response.json();
        document.getElementById("projected").value = amount;
    } else {
        alert("Something went wrong");
    }
}

async function reviewRequests () {
    setNav();
    let url = baseUrl + "/reimbursement/employee?";
    url += 'employeeId=' + loggedUser.employeeId;

    mainNav.innerHTML = "";

    let response = await fetch(url, {method: 'GET'});

    if (response.status === 200) {
        let RR_requests = await response.json();

        if (RR_requests.length > 0) {
            let table = document.createElement('table');
            table.setAttribute('classname', 'tabledata')

        table.innerHTML = `
            <tr class = tabledata>
                <th>Reimbursement ID</th>
                <th>Urgency</th>
                <th>Employee ID</th>
                <th>Status</th>
                <th>Cost</th>
                <th>Type</th>
                <th>Location</th>
                <th>Date</th>
                <th>Time Off Start</th>
                <th>Time Off End</th>
                <th>Reviewer Comments</th>
                <th></th>
            </tr>
        `;

        for (let RR_request of RR_requests) {
            let tr = document.createElement('tr');
            tr.innerHTML = `
                <td>${RR_request.reimbursementId}</td>
                <td>${RR_request.urgency.urgencyName}</td>
                <td>${RR_request.employee.employeeId}</td>
                <td>${RR_request.reimbursementStatus.statusName}</td>
                <td>${RR_request.reimbursementCost}</td>
                <td>${RR_request.reimbursementEventType.eventName}</td>
                <td>${RR_request.reimbursementLocation}</td>
                
                <td>${RR_request.reimbursementDate.month.substring(0, 3)} 
                    ${RR_request.reimbursementDate.dayOfMonth}, 
                    ${RR_request.reimbursementDate.year}</td>
                    
                <td>${RR_request.timeoffStart.month.substring(0, 3)}
                    ${RR_request.timeoffStart.dayOfMonth}
                    ${RR_request.timeoffStart.year}</td>
                    
                <td>${RR_request.timeoffEnd.month.substring(0, 3)}
                    ${RR_request.timeoffEnd.dayOfMonth}
                    ${RR_request.timeoffEnd.year}</td>
                    
                <td>${RR_request.notes}</td>
            `;

            if (!(RR_request.reimbursementStatus === "denied") && !(RR_request.reimbursementStatus === "approved")) {
                let approveBtn = document.createElement('button');
                approveBtn.type = 'button';
                approveBtn.id = RR_request.employee.employeeId + '_' + RR_request.reimbursementId;
                approveBtn.textContent = 'Approve Request';
                approveBtn.disabled = !loggedUser;

                let denyBtn = document.createElement('button');
                denyBtn.type = 'button';
                denyBtn.id = RR_request.employee.employeeId + '_' + RR_request.reimbursementId;
                denyBtn.textContent = 'Deny Request';
                denyBtn.disabled = !loggedUser;


                let btnTd = document.createElement('td');

                if (loggedUser.rank.rankId === 3) {
                    let alterBtn = document.createElement('button');
                    alterBtn.type = 'button';
                    alterBtn.id = RR_request.employee.employeeId + '_' + RR_request.reimbursementId;
                    alterBtn.textContent = 'Alter Request';
                    alterBtn.disabled = !loggedUser;
                    btnTd.appendChild(alterBtn);
                    alterBtn.addEventListener('click', alterRequest);
                }

                btnTd.appendChild(approveBtn);
                btnTd.appendChild(denyBtn);

                tr.appendChild(btnTd);

                approveBtn.addEventListener('click', approveRequest)
                denyBtn.addEventListener('click', denyRequest);
            }
            table.appendChild(tr);
        }
            mainNav.appendChild(table);
        } else {
            mainNav.innerHTML = "You do not currently have any requests to review."
        }
    } else {
        mainNav.innerHTML = "THERE WAS AN ERROR RETRIEVING INFORMATION FROM THE SERVER."
    }
}

async function viewYourRequests () {
    setNav();
    let url = baseUrl + "/reimbursement/empRRS?";

    url += 'employeeId=' + loggedUser.employeeId;

    mainNav.innerHTML = "";

    let response = await fetch(url, {method: 'GET'});

    if (response.status === 200) {
        let RR_requests = await response.json();

        let table = document.createElement('table');

        if (RR_requests.length > 0) {
            table.innerHTML = `
                <tr>
                    <th>Reimbursement ID</th>
                    <th>Employee ID</th>
                    <th>Status</th>
                    <th>Cost</th>
                    <th>Type</th>
                    <th>Location</th>
                    <th>Date</th>
                    <th>Time Off Start</th>
                    <th>Time Off End</th>
                    <th>Comments</th>
                </tr>
            `;

            for (let RR_request of RR_requests) {
                let tr = document.createElement('tr');
                tr.innerHTML = `
                    <td>${RR_request.reimbursementId}</td>
                    <td>${RR_request.employee.employeeId}</td>
                    <td>${RR_request.reimbursementStatus.statusName}</td>
                    <td>${RR_request.reimbursementCost}</td>
                    <td>${RR_request.reimbursementEventType.eventName}</td>
                    <td>${RR_request.reimbursementLocation}</td>
                    
                    <td>${RR_request.reimbursementDate.month.substring(0, 3)} 
                        ${RR_request.reimbursementDate.dayOfMonth}, 
                        ${RR_request.reimbursementDate.year}</td>
                        
                    <td>${RR_request.timeoffStart.month.substring(0, 3)}
                        ${RR_request.timeoffStart.dayOfMonth}
                        ${RR_request.timeoffStart.year}</td>
                        
                    <td>${RR_request.timeoffEnd.month.substring(0, 3)}
                        ${RR_request.timeoffEnd.dayOfMonth}
                        ${RR_request.timeoffEnd.year}</td>
                        
                    <td>${RR_request.notes}</td>
                `;

                secondaryNav.innerHTML = `
                    <p>You were approved for $${loggedUser.aid}</p>
                `

                let deleteBtn = document.createElement('button');
                deleteBtn.type = 'button';
                deleteBtn.id = RR_request.employee.employeeId + '_' + RR_request.reimbursementId;;
                deleteBtn.textContent = 'Delete Request';
                deleteBtn.disabled = RR_request.reimbursementStatus.statusId === 4;

                tr.appendChild(deleteBtn);
                deleteBtn.addEventListener('click', deleteRequest);
                table.appendChild(tr);
            }
            mainNav.appendChild(table);
        } else {
            mainNav.innerHTML = "You do not have any records in our system."
        }
    } else {
        mainNav.innerHTML = "THERE WAS AN ERROR RETRIEVING INFORMATION FROM THE SERVER."
    }
}

async function deleteRequest() {
    let btnId = event.target.id;
    let index = btnId.indexOf('_')
    let id = btnId.slice(index + 1)
    console.log(id);
    if (confirm('You wish to delete request no.' + event.target.id.substring(0, event.target.id.indexOf('_')) + '?'))
    {
        let url = baseUrl + '/reimbursement/delete?';
        url += 'rrId=' + id + '&'

        let response = await fetch(url);

        if (response.status === 200) {
            alert("You've successfully deleted the request")
            mainNav.innerHTML = "";
        } else {
            alert("Something went wrong")
        }
    }
}

async function alterRequest() {
    let btnId = event.target.id;
    let index = btnId.indexOf('_')
    let id = btnId.slice(index + 1)
    console.log(id);
    if (confirm('You wish to alter the amount request no.' + event.target.id.substring(0, event.target.id.indexOf('_')) + '?'))
    {
        let value = prompt("Enter the new offer amount");
        let comment = prompt("Enter a comment");
        let url = baseUrl + '/reimbursement/alter?';
        url += 'rrId=' + id
        url += '&value=' + value;
        url += '&sender=' + loggedUser.employeeId;

        if (comment !== '') {
            url += '&comment=' + comment;
        }

        let response = await fetch(url);

        if (response.status === 200) {
            alert("You've successfully altered the request")
            mainNav.innerHTML = "";
        } else {
            alert("Something went wrong")
        }
    }
}

async function approveRequest() {
    let btnId = event.target.id;
    let index = btnId.indexOf('_')
    let id = btnId.slice(index + 1)
    console.log(id);
    if (confirm('You wish to approve request no.' + event.target.id.substring(0, event.target.id.indexOf('_')) + '?'))
    {
        let comment = prompt("Enter a comment");
        let url = baseUrl + '/reimbursement/accept?';
        url += 'rrId=' + id + '&'
        url += 'employeeId=' + loggedUser.employeeId;
        if (loggedUser.reportsTo !== null) {
            url += '&reportsto=' + loggedUser.reportsTo.employeeId;
        }
        if (comment !== '') {
            url += '&comment=' + comment;
        }

        let response = await fetch(url);

        if (response.status === 200) {
            alert("You've successfully approved the request")
            mainNav.innerHTML = "";
        } else {
            alert("Something went wrong")
        }
    }
}

async function denyRequest() {
    let btnId = event.target.id;
    let index = btnId.indexOf('_')
    let id = btnId.slice(index + 1)
    console.log(id);
    if (confirm('You wish to deny request no.' + event.target.id.substring(0, event.target.id.indexOf('_')) + '?'))
    {
        let comment = prompt("Enter a comment");
        let url = baseUrl + '/reimbursement/deny?';
        url += 'rrId=' + id + '&'
        url += 'employeeId=' + loggedUser.employeeId;
        if (comment !== '') {
            url += '&comment=' + comment;
        }
        let response = await fetch(url);

        if (response.status === 200) {
            alert("You've successfully denied the request")
            mainNav.innerHTML = "";
        } else {
            alert("Something went wrong")
        }
    }
}

function employeeRegisterMenu() {
    setNav();
    mainNav.innerHTML = "";
    mainNav.innerHTML = `
            <form>
                <li><label for="username">Employee Username: </label></li>
                <input id="rusername" name="rusername" type="text" required/><br>
                <li><label for="password"> Password: </label></li>
                <input id="rpassword" name="rpassword" type="password" required/><br>
                <li><label for="department"/>Department</li>
                <select id="department" name="department" required>
                        <option value="4">Human Resources</option>
                        <option value="1">Management</option>
                        <option value="2">IT</option>
                        <option value="3">Accounting and Finance</option>
                        <option value="5">Marketing</option>
                </select><br>
                <li><label for="rank">Rank: </label></li>
                <select id="rank" name="rank" required>
                <option value="0">Employee</option>
                <option value="1">Supervisor</option>
                <option value="2">Department Head</option>
                <option value="3">BenCo</option>
                </select><br>
                <li><label for="reportsto">Supervisor ID: </label></li>
                <input id="reportsto" name="reportsto"/><br>
                <li><label for="firstname">First Name: </label></li>
                <input id="firstname" name="first_name" required><br>
                <li><label for="lastname">Last Name: </label></li>
                <input id="lastname" name="last_name" required><br>
                <li><label for="address">Address: </label></li>
                <input id="address" name="address" required><br>
                <button type="button" id="registerBtn">Register</button>
            </form>
        `;

    let registerBtn = document.getElementById('registerBtn');
    registerBtn.onclick = register;
}

async function register() {
    let url = baseUrl + '/register/register?';
    if ((document.getElementById("rusername").value === "")) {
        alert("Username is missing");
        return;
    } else {
        url += 'username=' + document.getElementById('rusername').value + '&';
    }

    if ((document.getElementById("rpassword").value === "")) {
        alert("Password is missing");
        return;
    } else {
        url += 'password=' + document.getElementById('rpassword').value + '&';
    }

    if ((document.getElementById("department").value === "")) {
        alert("Department is missing");
        return;
    } else {
        url += 'department=' + document.getElementById('department').value + '&';
    }

    if ((document.getElementById("firstname").value === "")) {
        alert("First name is missing");
        return;
    } else {
        url += 'firstname=' + document.getElementById('firstname').value + '&';
    }

    if ((document.getElementById("lastname").value === "")) {
        alert("Last name is missing");
        return;
    } else {
        url += 'lastname=' + document.getElementById('lastname').value + '&';
    }

    if ((document.getElementById("address").value === "")) {
        alert("Address is missing");
        return;
    } else {
        url += 'address=' + document.getElementById('address').value + '&';
    }

    if ((document.getElementById("rank").value === "")) {
        alert("Rank is missing");
        return;
    } else {
        url += 'rank=' + document.getElementById('rank').value + '&';
    }

    if (!(document.getElementById("reportsto").value === "")) {
        url += 'reportsto=' + document.getElementById("reportsto").value;
    } else {
        url += 'reportsto=' + 'null';
    }

    console.log(url);

    let response = await fetch(url, {method: 'POST'});

    switch (response.status) {
        case 200: // successful
            nav.innerHTML = "You've sucessfully registered an employee account. <br> Redirecting you to login!";
            setTimeout(function () {
                setNav();
                mainNav.innerHTML = "";
            }, 3000);
            break;
        default: // other error
            alert('Something went wrong.');
            break;
    }
}

async function registerRR() {
    let url = baseUrl + '/reimbursementrequest/request?';
    if ((document.getElementById("cost").value === "")) {
        alert("Cost is missing");
        return;
    } else {
        url += 'cost=' + document.getElementById('cost').value + '&';
    }

    if ((document.getElementById("type").value === "")) {
        alert("Event type is missing");
        return;
    } else {
        url += 'type=' + document.getElementById('type').value + '&';
    }

    if ((document.getElementById("startdate").value === "")) {
        alert("Start date is missing");
        return;
    } else {
        url += 'startdate=' + document.getElementById('startdate').value + '&';
    }

    if ((document.getElementById("enddate").value === "")) {
        alert("End date is missing");
        return;
    } else {
        url += 'enddate=' + document.getElementById('enddate').value + '&';
    }

    if ((document.getElementById("location").value === "")) {
        alert("Location is missing");
        return;
    } else {
        url += 'location=' + document.getElementById('location').value + '&';
    }

    url += 'employeeid=' + loggedUser.employeeId + '&';
    url += 'department=' + loggedUser.department.departmentId + '&';
    url += 'reportsto=' + loggedUser.reportsTo.employeeId;

    console.log(url);

    let response = await fetch(url, {method: 'POST'});

    switch (response.status) {
        case 200: // successful
            nav.innerHTML = "You've successfully registered a reimbursement request. <br>";
            setTimeout(function () {
                setNav();
                mainNav.innerHTML = "";
            }, 5000);
            break;
        default: // other error
            alert('Something went wrong.');
            break;
    }
}

async function login() {
    mainNav.innerHTML = "";
    let url = baseUrl + '/login/login?';
    url += 'username=' + document.getElementById('username').value + '&';
    url += 'password=' + document.getElementById('password').value;
    console.log(url);
    let response = await fetch(url, {method: 'POST'});

    switch (response.status) {
        case 200:
            loggedUser = await response.json();
            setNav();
            break;
        case 400:
            alert('Incorrect password, try again.');
            document.getElementById('pass').value = '';
            break;
        case 404:
            alert('That user does not exist.');
            document.getElementById('username').value = '';
            document.getElementById('password').value = '';
            break;
        default:
            alert('Something went wrong.');
            break;
    }
}

async function logout() {
    mainNav.innerHTML = "";
    loggedUser = null;
    setNav();
}