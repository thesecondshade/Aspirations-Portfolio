ACCOUNT=$(shell aws sts get-caller-identity --query Account --output text)
GIT_SHA=$(shell git rev-parse HEAD)


deploy-artifact: build
	curl -s http://169.254.169.254/config/arn:aws:iam::056684691971:role/admin/resources/sysops-team-access-SysOpsT2Role-75ZYQ0CEGZKI > ~/.aws/sysopst2.config
	docker run -e AWS_DEFAULT_REGION=us-east-1 -it --rm \
		-v `ls ~/.aws/sysopst2.config`:/root/.aws/config \
		-v `pwd`:/tmp/ \
		-e GIT_SHA="${GIT_SHA}" \
		governmentpaas/awscli ash /tmp/push.sh

build:
	sh ../common/build.sh

clean:
	rm -f ./deployment.zip
