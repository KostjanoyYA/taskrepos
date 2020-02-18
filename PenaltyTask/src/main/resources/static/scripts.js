const penaltyFormIds = [
	'firstName',
	'lastName',
	'middleName',
];

const serverUrl = 'http://localhost';
const serverPort = '8080';
const apiMethods = {
	penaltyEvents: 'penaltyevents',
	statistics: 'statistics',
};

const penaltyTableCellOrder = [
	'penaltyEventID',
	'fineCharge',
	'fineType',
	'penaltyEventTimeStamp',
	'fullStateNumber',
	'carMake',
	'carModel',
	'lastName',
	'firstName',
	'middleName',
];

const statisticTableCellOrder = [
	'topPlace',
	'occurrencesNumber',
	'fineType',
	'fineID',
];

const createTableRow = (cells, order, opt_isHeader) => {
	const tableRow = document.createElement('tr');
	const tagToCreate = opt_isHeader ? 'th' : 'td';

	return order
		.map((key) => cells[key])
		.reduce((row, cellValue) => {
			const cell = document.createElement(tagToCreate);
			cell.innerText = String(cellValue);
			row.appendChild(cell);

			return row;
		}, tableRow)
};

const createPenaltyTableRow = (data, opt_isHeader) => createTableRow(data, penaltyTableCellOrder, opt_isHeader);
const createPenaltyTableHeader = () => createPenaltyTableRow({
	penaltyEventID: 'Уникальный идентификатор',
	fineCharge: 'Сумма',
	fineType: 'Причина',
	penaltyEventTimeStamp: 'Время',
	fullStateNumber: 'Гос. номер',
	carMake: 'Марка',
	carModel: 'Модель',
	lastName: 'Фамилия',
	firstName: 'Имя',
	middleName: 'Отчество'
}, true);

const createStatisticTableRow = (data, opt_isHeader) => createTableRow(data, statisticTableCellOrder, opt_isHeader);
const createStatisticTableHeader = () => createStatisticTableRow({
	topPlace: 'Место top штрафа',
	occurrencesNumber: 'Количество упоминаний',
	fineType: 'Тип штрафа',
	fineID: 'id штрафа',
}, true);


const sendRequest = (options) => {
	const { path = '', queryParams} = options;
	const stringParams = queryParams ? `${Object.keys(queryParams).map(key =>
		`${encodeURIComponent(key)}=${encodeURIComponent(queryParams[key])}`).join('&')}` : null;
	const fullPath = `${serverUrl}${serverPort ? ':' + serverPort : ''}/${path}${stringParams ? `?${stringParams}` : ''}`;

	return fetch(fullPath).then(response => {
		if (response.status > 200) {
			return Promise.reject(response);
		}

		return response.json();
	});
};

const getFullName = () => {
	return penaltyFormIds.reduce((acc, key) => {
		const value = document.getElementById(key).value;
		if (value.length) {
			acc[key] = value;
		}

		return acc;
	}, {});
};

const getStateNumber = () => {
	const fullStateNumber = document.getElementById('stateNumber').value;

	return fullStateNumber.length ? { fullStateNumber } : {};
};


function flushForm () {
	[...penaltyFormIds, 'stateNumber', 'statisticCount'].forEach(key => document.getElementById(key).value = '');
	flushTable();
}

function flushTable () {
	const tableElement = document.getElementById('table');
	while (tableElement.firstChild) {
		tableElement.removeChild(tableElement.firstChild);
	}
	tableElement.style.visibility = 'hidden';
}

const renderError = (err) => {
	const tableElement = document.getElementById('table');
	const errorElement = document.createElement('td');
	errorElement.className = 'error';
	errorElement.innerText = `Server ERROR: error code: ${err.status}, ${err.statusText}`;
	tableElement.appendChild(errorElement);
	tableElement.style.visibility = 'visible';
};

const renderPenaltyTable = (data) => {
	const tableElement = document.getElementById('table');
	tableElement.appendChild(createPenaltyTableHeader());
	tableElement.style.visibility = 'visible';

	data.reduce((table, dataChunk) => {
		table.appendChild(createPenaltyTableRow(dataChunk));
		return table;
	}, tableElement);
};

const renderStatisticTable = (data) => {
	const tableElement = document.getElementById('table');
	tableElement.appendChild(createStatisticTableHeader());
	tableElement.style.visibility = 'visible';

	data.reduce((table, dataChunk) => {
		table.appendChild(createStatisticTableRow(dataChunk));
		return table;
	}, tableElement);
};

function onPenaltyClick () {
	const fullName = getFullName();
	const fullStateNumber = getStateNumber();
	flushTable();

	sendRequest({
		path: apiMethods.penaltyEvents,
		queryParams: {
			...fullName,
			...fullStateNumber
		}
	}).then(renderPenaltyTable, renderError);
}

function onStatisticClick () {
	const count = document.getElementById('statisticCount').value;
	flushTable();

	sendRequest({
		path: `${apiMethods.statistics}/${count}`
	}).then(renderStatisticTable, renderError);
};
