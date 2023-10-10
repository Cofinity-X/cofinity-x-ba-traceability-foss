#!/bin/bash

BLUE='\033[0;34m'
NC='\033[0m' # No Color

HELM_CHART_NAME=tracex-app
HELM_CHART=$(helm list -q -f "$HELM_CHART_NAME")

echo -e "${BLUE}Update chart dependency${NC}"
helm dependency update

if [ "$HELM_CHART" != "" ];
then
    echo -e "${BLUE}Un-installing helm chart: $HELM_CHART_NAME ${NC}"
    helm uninstall $HELM_CHART_NAME --namespace tracex
fi

echo -e "${BLUE}Installing helm chart: $HELM_CHART_NAME ${NC}"
helm install $HELM_CHART_NAME \
    --set install.keycloak=true \
    --set postgresql.enabled=true \
    --set pgadmin4.enabled=true \
    --set irs-helm.enabled=true \
    --set tractusx-connector.enabled=true \
    --set edc-postgresql.enabled=true \
    --namespace tracex --create-namespace .

echo -e "${BLUE}Installing edc-vault helm chart${NC}"
helm install $HELM_CHART_NAME --set install.edc.vault=true --namespace tracex --create-namespace .

echo -e "${BLUE}Waiting for the deployments to be available${NC}"
kubectl wait deployment -n tracex --for condition=Available --timeout=90s keycloak
kubectl wait deployment -n tracex --for condition=Available --timeout=90s edc-postgresql
kubectl wait deployment -n tracex --for condition=Available --timeout=90s tractusx-connector
kubectl wait deployment -n tracex --for condition=Available --timeout=90s irs-helm

echo -e "Deployments completed..."

# minikube start --memory 10000 --cpus 2
