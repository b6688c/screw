import { Reducer, Effect } from 'umi';
import { 
    queryAppServer, 
    editAppServer, 
    addAppServer, 
    deleteAppServer, 
    loadDataSource, 
    testConnect,
    queryServerDirectory,
    isExist } from '@/pages/configurations/server/services/index'
import { message } from 'antd';
import { AppServer } from '@/pages/configurations/server/data';
import { ConnectState } from '@/models/connect';
import _ from 'lodash';

export interface AppServerState {
    isLoading: boolean;
    appServers: AppServer[];
};

export interface AppServerModelType {
    namespace: 'server';
    state: AppServerState;
    effects: {
        queryAppServer: Effect;
        editAppServer: Effect;
        addAppServer: Effect;
        deleteAppServer: Effect;
        loadDataSource: Effect;
        testConnect: Effect;
        queryServerDirectory: Effect;
        isExist: Effect;
    };
    reducers: {
        setState: Reducer<AppServerState>;
    };
}

const defaultAppServerState: AppServerState = {
    isLoading: true,
    appServers: []
}

const AppServerModel: AppServerModelType = {
    namespace: 'server',
    state: defaultAppServerState,
    effects: {
        *queryAppServer({ payload, callback }, { call, put, select }) {
            const result = yield call(queryAppServer, payload);
            const { success, msg, data } = result;
            if (!success) {
                message.error(`${msg}`);
            }
            yield put({
                type: 'setState',
                payload: {
                    isLoading: false,
                    appServers: data
                },
            });
            yield select((state: ConnectState) => {
                callback && callback(state.server);
            });
        },
        *editAppServer({ payload, callback }, { call, put, select }) {
            const result = yield call(editAppServer, payload);
            yield callback && callback(result);

        },
        *addAppServer({ payload, callback }, { call, put, select }) {
            const result = yield call(addAppServer, payload);
            yield callback && callback(result);
        },
        *deleteAppServer({ payload, callback }, { call, put, select }) {
            const result = yield call(deleteAppServer, payload);
            callback && callback(result);
        },
        *loadDataSource({ payload, callback}, { call }) {
            const result = yield call(loadDataSource);
            callback && callback(result);
        },
        *testConnect({ payload, callback}, { call }) {
            let id = '';
            if (!_.isEmpty(payload)) {
                if (payload.hasOwnProperty('dataSource')) {
                    id = payload.dataSource;
                }
            }
            const result = yield call(testConnect, id);
            callback && callback(result);
        },
        *queryServerDirectory({ payload, callback}, { call }) {
            const result = yield call(queryServerDirectory);
            callback && callback(result);
        },
        *isExist({ payload, callback }, { call }) {
            const result = yield call(isExist, payload);
            callback && callback(result);
        }
    },
    reducers: {
        setState(state, { payload }): AppServerState {
            return {
                ...payload
            };
        },
    },
}


export default AppServerModel;