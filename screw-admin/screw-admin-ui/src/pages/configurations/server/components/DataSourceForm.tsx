import React from 'react';
import ProForm, { ProFormRadio } from '@ant-design/pro-form';
import { Result, Button } from 'antd';
import { DataSource } from '@/pages/configurations/datasource/data'
import _ from 'lodash'

const ServerForm: React.FC<any> = ( props ) => {

    const dataSources: DataSource[] = props.dataSources;

    const render = (): React.ReactNode => {
        if (_.isEmpty(dataSources)) {
            return (
                <Result
                    status="warning"
                    title="数据源为空，请先配置数据源"
                    extra={
                    <Button type="primary" key="console" onClick={(event) => {
                        event.stopPropagation();
                        props.history.push('/config/datasource')
                    }}>
                        前往配置
                    </Button>
                    }                
                />
            )
        } else {
            return (
                <ProForm.Group>
                    <ProFormRadio.Group
                        name="dataSource"
                        label="数据源"
                        radioType="button"
                        options={
                            dataSources.map((dataSource) => {
                                return {label: dataSource.datasourceName, value: dataSource.id}
                            })
                        }
                    />
                </ProForm.Group>
            )
        }
    }

    return (
        <div>
            {render()}
        </div>
    )
}

export default ServerForm;
